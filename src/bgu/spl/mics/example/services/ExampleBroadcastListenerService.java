package bgu.spl.mics.example.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.example.messages.ExampleBroadcast;

public class ExampleBroadcastListenerService extends MicroService {

    private int mbt;

    public ExampleBroadcastListenerService(String name, String[] args) {
        super(name);

        if (args.length != 1) {
            throw new IllegalArgumentException("Listener expecting a single argument: mbt (the number of requests to answer before termination)");
        }

        try {
            mbt = Integer.parseInt(args[0]);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Listener expecting the argument mbt to be a number > 0, instead received: " + args[0]);
        }

        if (mbt <= 0) {
            throw new IllegalArgumentException("Listener expecting the argument mbt to be a number > 0, instead received: " + args[0]);
        }
    }

    @Override
    protected void initialize() {
        System.out.println("Listener " + getName() + " started");
        
        subscribeBroadcast(ExampleBroadcast.class, message -> {
            mbt--;
            System.out.println("Listener " + getName() + " got a new message from " + message.getSenderId() + "! (mbt: " + mbt + ")");
            if (mbt == 0) {
                System.out.println("Listener " + getName() + " terminating.");
                terminate();
            }
        });
    }

}
