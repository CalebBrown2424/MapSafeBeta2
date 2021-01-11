package com.example.location;

public class CountAdder {
    private double value;

    public void setValue(double value) {
        this.value = value;
    }

    public CountAdder()
    {
        value = 0.1;
    }
    public void increaseCount(){
        value += 0.1;

    }
    public void increaseCountSmall(){
        value += 0.01;
    }
    public void decreaseCountSmall(){
        value -= 0.01;
        if(value <= 0)
        {
            value = 0;
        }
    }
    public void decreaseCount(){
        value -= 0.1;
        if(value <= 0)
        {
            value = 0;
        }
    }

    public double getCount()
    {
        return value;
    }
}