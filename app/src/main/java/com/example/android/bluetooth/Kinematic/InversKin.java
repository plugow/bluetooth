package com.example.android.bluetooth.Kinematic;

public class InversKin {


    public static double[] inverse(double x, double y, double z){
        double[] angleValue=new double[3];
        int armLength1=64;
        int armLength2=84;
        int armLength3=83;
        int armLength4=45;


        angleValue[0]=Math.atan2(y,x);
        double zdod=z-armLength1;
        double a=Math.sqrt(Math.pow(x,2)+Math.pow(y,2))-armLength4;
        angleValue[2]=-Math.acos((Math.pow(a,2)+Math.pow(zdod,2)-Math.pow(armLength2,2)-Math.pow(armLength3,2))/(2*armLength2*armLength3));
        double beta=Math.atan2(zdod,a);
        double psi=Math.acos((Math.pow(a,2)+Math.pow(zdod,2)+Math.pow(armLength2,2)-Math.pow(armLength3,2))/(2*armLength2*Math.sqrt(Math.pow(a,2)+Math.pow(zdod,2))));
        angleValue[1]=beta+psi;


        return angleValue;
    }

}