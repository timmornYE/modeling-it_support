package uibk.informatik.pmodeling.itsupport;

import org.camunda.bpm.engine.cdi.BusinessProcess;


 
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.io.Serializable;
 
@Named
@ConversationScoped
public class RegisterTicketController implements Serializable { 
	
	private static  final long serialVersionUID = 1L;
	 
	// Inject the BusinessProcess to access the process variables
	@Inject
	private BusinessProcess businessProcess;
	
	// Inject the EntityManager to access the persisted order
	@PersistenceContext
	private EntityManager entityManager;
	 
	// Inject the OrderBusinessLogic to update the persisted order
	@Inject
	private SupportBusinessLogic supportBusinessLogic;
	
	// Caches the OrderEntity during the conversation
	private TicketEntity ticketEntity;
	
	public TicketEntity getTicketEntity() {
		if (ticketEntity == null) {
			// Load the order entity from the database if not already cached
			ticketEntity = supportBusinessLogic.getTicket((Long) businessProcess.getVariable("orderId"));
		}
		return ticketEntity;
	}
	 
	public void submitForm() throws IOException {
		// Persist updated order entity and complete task form
		supportBusinessLogic.mergeOrderAndCompleteTask(ticketEntity);
	}

}
