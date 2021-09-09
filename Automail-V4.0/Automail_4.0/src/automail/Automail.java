package automail;

import simulation.IMailDelivery;

public class Automail {

    private Robot[] robots;
    private MailPool mailPool;
    
    public Automail(MailPool mailPool, IMailDelivery delivery, int numRegRobots, int numFastRobots, int numBulkRobots) {  	
    	/** Initialize the MailPool */
    	
    	this.mailPool = mailPool;
    	
    	/** Initialize robots, currently only regular robots */
    	robots = new Robot[numRegRobots + numFastRobots + numBulkRobots];   // Add FastRobots & BulkRobots
    	for (int i = 0; i < numRegRobots; i++) robots[i] = new Robot(delivery, mailPool, i);
    	for (int i = numRegRobots; i < numRegRobots + numFastRobots; i++) robots[i] = new FastRobot(delivery, mailPool, i);
        for (int i = numRegRobots + numFastRobots; i < numRegRobots + numFastRobots + numBulkRobots; i++) robots[i] = new BulkRobot(delivery, mailPool, i);
    }

    public Robot[] getRobots() {
        return robots;
    }

    public MailPool getMailPool() {
        return mailPool;
    }
}
