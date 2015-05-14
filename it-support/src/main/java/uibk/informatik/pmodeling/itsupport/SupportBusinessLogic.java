package uibk.informatik.pmodeling.itsupport;

import org.camunda.bpm.engine.delegate.DelegateExecution;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Map;
 
@Stateless
@Named
public class SupportBusinessLogic {
	// Inject the entity manager
	@PersistenceContext
	private EntityManager entityManager;
	
	public void persistSupportRequest(DelegateExecution delegateExecution) {
		// Create new order instance
		TicketEntity orderEntity = new TicketEntity();
	 
		// Get all process variables
		Map<String, Object> variables = delegateExecution.getVariables();
	 
	    // Set order attributes
	    orderEntity.setTeachercode((String) variables.get("teachercode"));
	    orderEntity.setSuptype((String) variables.get("suptype"));
	 
	    /*
	      Persist order instance and flush. After the flush the
	      id of the order instance is set.
	    */
	    entityManager.persist(orderEntity);
	    entityManager.flush();
	 
	    // Remove no longer needed process variables
	    delegateExecution.removeVariables(variables.keySet());
	 
	    // Add newly created order id as process variable
	    delegateExecution.setVariable("orderId", orderEntity.getId());
	}

}
