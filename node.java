public class node
{
    public double mass;
    public double x;
    public double y;
    public double xVelocity;
    public double yVelocity;
    public double startX;
    public double startY;
    public double velCap;

    public node(double setMass, double startx, double starty, double velocityCap)
    {
        mass = setMass;
        x = startx;
        y = starty;
        startX = startx;
        startY = starty;
        xVelocity = 0;
        yVelocity = 0;
        velCap = velocityCap;
    }

    public void updatePosition(double dTime, double ground, terrain t, int tick)
    {

        x = x + (xVelocity * dTime);
        y = y + (yVelocity * dTime);

        if(yVelocity > velCap)
        {
            yVelocity = velCap;
        }
        if(xVelocity > velCap)
        {
            xVelocity = velCap;
        }
        if(yVelocity < -velCap)
        {
            yVelocity = -velCap;
        }
        if(xVelocity < -velCap)
        {
            xVelocity = -velCap;
        }

        if(y > ground + t.heightFunction(x))
        {
            y = ground + t.heightFunction(x);
            yVelocity = 0;
        }


    }

    public void applyForce(double xForce, double yForce, double dTime)
    {
        xVelocity = xVelocity + (xForce*dTime/mass);
        yVelocity = yVelocity + (yForce*dTime/mass);


    }
}
