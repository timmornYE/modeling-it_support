package uibk.informatik.pmodeling.itsupport;

import org.camunda.bpm.engine.cdi.jsf.TaskForm;
import org.camunda.bpm.engine.delegate.DelegateExecution;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
 
@Stateless
@Named
public class SupportBusinessLogic {
	
	private static Logger LOGGER = Logger.getLogger(SupportBusinessLogic.class.getName());
	
	// Inject the entity manager
	@PersistenceContext
	private EntityManager entityManager;
	
	// Inject task form available through the camunda cdi artifact
	@Inject
	private TaskForm taskForm;
	
	public void persistSupportRequest(DelegateExecution delegateExecution) {
		// Create new order instance
		TicketEntity ticketEntity = new TicketEntity();
	 
		// Get all process variables
		Map<String, Object> variables = delegateExecution.getVariables();
	 
	    // Set order attributes
	    ticketEntity.setTeachercode((String) variables.get("teachercode"));
	    ticketEntity.setSuptype((String) variables.get("suptype"));
	    ticketEntity.setSupDescription((String) variables.get("supDescription"));
	 
	    /*
	      Persist order instance and flush. After the flush the
	      id of the order instance is set.
	    */
	    entityManager.persist(ticketEntity);
	    entityManager.flush();
	 
	    // Remove no longer needed process variables
	    delegateExecution.removeVariables(variables.keySet());
	 
	    // Add newly created order id as process variable
	    delegateExecution.setVariable("orderId", ticketEntity.getId());
	}
	
	public TicketEntity getTicket(Long orderId) {
	    // Load order entity from database
	    return entityManager.find(TicketEntity.class, orderId);
	}
	 
	/*
		Merge updated order entity and complete task form in one transaction. This ensures
	    that both changes will rollback if an error occurs during transaction.
	*/
	public void mergeOrderAndCompleteTask(TicketEntity orderEntity) {
	    // Merge detached order entity with current persisted state
	    entityManager.merge(orderEntity);
	    try {
	      // Complete user task from
	      taskForm.completeTask();
	    } catch (IOException e) {
	      // Rollback both transactions on error
	      throw new RuntimeException("Cannot complete task", e);
	    }
	}
	
	public void sendRequestForSupporter(DelegateExecution delegateExecution) {
	    TicketEntity ticket = getTicket((Long) delegateExecution.getVariable("orderId"));
	    LOGGER.log(Level.INFO, "\n\n\nSending Email:\nTeacher with the name {0} has created a ticket with number {1} and request support for type {2}\n\n\n", new String[]{ticket.getTeachercode(), String.valueOf(ticket.getId()), ticket.getSuptype()});
	  }

}
