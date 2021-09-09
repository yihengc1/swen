package automail;

import exceptions.ExcessiveDeliveryException;
import exceptions.ItemTooHeavyException;
import exceptions.RobotHasNoHands;
import simulation.IMailDelivery;

import java.util.Vector;

public class BulkRobot extends Robot {
    public static final int MAX_MAILITEMS = 5;
    private Vector<MailItem> tubeItems;

    public BulkRobot(IMailDelivery delivery, MailPool mailPool, int number){
        super(delivery, mailPool, number);

        this.id = "B" + number;
        this.tubeItems = new Vector<MailItem>(5);
    }

    @Override
    public void operate() throws ExcessiveDeliveryException {
        switch(current_state) {
            /** This state is triggered when the robot is returning to the mailroom after a delivery */
            case RETURNING:
                /** If its current position is at the mailroom, then the robot should change state */
                if(current_floor == Building.getInstance().getMailroomLocationFloor()){
                    /** Tell the sorter the robot is ready */
                    mailPool.registerWaiting(this);
                    changeState(RobotState.WAITING);
                } else {
                    /** If the robot is not at the mailroom floor yet, then move towards it! */
                    moveTowards(Building.getInstance().getMailroomLocationFloor());
                    break;
                }
            case WAITING:
                /** If the StorageTube is ready and the Robot is waiting in the mailroom then start the delivery */
                if(!isEmpty() && receivedDispatch){
                    receivedDispatch = false;
                    deliveryCounter = 0; // reset delivery counter

                    deliveryItem = tubeItems.lastElement();

                    setDestination();
                    changeState(RobotState.DELIVERING);
                }
                break;
            case DELIVERING:
                if(current_floor == destination_floor){ // If already here drop off either way
                    /** Delivery complete, report this to the simulator! */
                    delivery.deliver(this, deliveryItem, "");
                    tubeItems.remove(deliveryItem);
                    deliveryCounter++;
                    if(deliveryCounter > MAX_MAILITEMS) {  // Implies a simulation bug
                        throw new ExcessiveDeliveryException();
                    }

                    if (tubeItems.size() > 0) { // Deliver next in the tube
                        deliveryItem = tubeItems.lastElement();
                        setDestination();
                        changeState(RobotState.DELIVERING);
                    } else {
                        changeState(RobotState.RETURNING);
                    }

                } else {
                    /** The robot is not at the destination yet, move towards it! */
                    moveTowards(destination_floor);
                }
                break;
        }
    }
    @Override
    public String getIdTube() {
        return String.format("%s(%1d)", this.id, (tubeItems.size()));
    }

    @Override
    public void addToTube(MailItem mailItem) throws ItemTooHeavyException {
        assert(tubeItems.size() <= MAX_MAILITEMS);

        if (mailItem.weight > INDIVIDUAL_MAX_WEIGHT) throw new ItemTooHeavyException();
        else tubeItems.add(mailItem);
    }

    @Override
    public boolean isEmpty() {
        return (tubeItems.isEmpty());
    }

    @Override
    public void addToHand(MailItem mailItem) throws RobotHasNoHands {
        throw new RobotHasNoHands();
    }
}
