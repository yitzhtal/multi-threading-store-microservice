package bgu.spl.mics.example.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.example.messages.ExampleRequest;

public class ExampleRequestHandlerService extends MicroService {

    private int mbt;

    public ExampleRequestHandlerService(String name, String[] args) {
        super(name);

        if (args.length != 1) {
            throw new IllegalArgumentException("Request Handler expecting a single argument: mbt (the number of requests to answer before termination)");
        }

        try {
            mbt = Integer.parseInt(args[0]);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Request Handler expecting the argument mbt to be a number > 0, instead received: " + args[0]);
        }

        if (mbt <= 0) {
            throw new IllegalArgumentException("Request Handler expecting the argument mbt to be a number > 0, instead received: " + args[0]);
        }
    }

    @Override
    protected void initialize() {
        System.out.println("Request Handler " + getName() + " started");
        
        subscribeRequest(ExampleRequest.class, req -> {
            mbt--;
            System.out.println("Request Handler " + getName() + " got a new request from " + req.getSenderName() + "! (mbt: " + mbt + ")");
            complete(req, "Hello from " + getName());
            if (mbt == 0) {
                System.out.println("Request Handler " + getName() + " terminating.");
                terminate();
            }
        });
    }

}
