package bgu.spl.mics.example;

import bgu.spl.mics.MicroService;

public interface ServiceCreator {
    MicroService create(String name, String[] args);
}
