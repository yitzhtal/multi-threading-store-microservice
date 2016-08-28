package bgu.spl.mics.example.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleRequest;

public class ExampleMessageSenderService extends MicroService {

    private boolean broadcast;

    public ExampleMessageSenderService(String name, String[] args) {
        super(name);

        if (args.length != 1 || !args[0].matches("broadcast|request")) {
            throw new IllegalArgumentException("expecting a single argument: broadcast/request");
        }

        this.broadcast = args[0].equals("broadcast");
    }

    @Override
    protected void initialize() {
        System.out.println("Sender " + getName() + " started");
        
        if (broadcast) {
            sendBroadcast(new ExampleBroadcast(getName()));
            System.out.println("Sender " + getName() + " publish an event and terminates");
            terminate();
        } else {
            boolean success = sendRequest(new ExampleRequest(getName()), v -> {
                System.out.println("Sender " + getName() + " got notified about request completion with result: \"" + v + "\" - terminating");
                terminate();
            });

            if (success) {
                System.out.println("Sender " + getName() + " send a request and wait for its completion");
            }else {
                System.out.println("Sender " + getName() + " tried to send a request but no one cares - terminating");
                terminate();
            }
        }
    }

}
