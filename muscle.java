public class muscle
{
    public node startNode;
    public node endNode;

    public int startX;
    public int startY;

    public int endX;
    public int endY;

    public double strengthCoefficient;
    public double flexibility;

    private double extendedLength;
    private double retractedLength;
    public double relaxedLength;

    public boolean relaxed;
    public boolean extending;

    public muscle(node start, node end, double strength, double flex)
    {
        startNode = start;
        endNode = end;
        strengthCoefficient = strength;
        flexibility = flex;

        relaxedLength = Math.sqrt(Math.pow(start.x-end.x,2) + Math.pow(start.y-end.y,2));
        extendedLength = relaxedLength + flex;
        retractedLength = relaxedLength - flex;

        relaxed = true;
        extending = false;
    }

    public void applyForces(double dTime)
    {
        //determine targetLength
        double targetLength;
        if(relaxed)
        {
            targetLength = relaxedLength;
        }
        else if (extending)
        {
            targetLength = extendedLength;
        }
        else
        {
            targetLength = retractedLength;
        }

        double currentLength = Math.sqrt(Math.pow(startNode.x-endNode.x,2) + Math.pow(startNode.y-endNode.y,2));

        double pull = Math.abs(targetLength - currentLength);

        double normalizedX = 0;
        double normalizedY = 0;

        normalizedX = (startNode.x - endNode.x) / currentLength;
        normalizedY = (startNode.y - endNode.y) / currentLength;

        if(Double.isNaN(normalizedX))
        {
            normalizedX = 0;
        }
        if(Double.isNaN(normalizedY))
        {
            normalizedY = 0;
        }

        if(targetLength < currentLength)
        {
            endNode.applyForce(normalizedX * pull * strengthCoefficient, normalizedY * pull *  strengthCoefficient, dTime);
            startNode.applyForce(normalizedX * pull *  strengthCoefficient * -1, normalizedY * pull *  strengthCoefficient * -1, dTime);
        }
        else
        {
            startNode.applyForce(normalizedX * pull * strengthCoefficient, normalizedY * pull *  strengthCoefficient, dTime);
            endNode.applyForce(normalizedX * pull *  strengthCoefficient * -1, normalizedY * pull *  strengthCoefficient * -1, dTime);
        }
    }

}
