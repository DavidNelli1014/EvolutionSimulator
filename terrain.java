public class terrain
{
    public enum type{linear, flat, sinusoidal};
    public type terrainType;
    public double value1;
    public double value2;
    public terrain(type terraintype, double Value1, double Value2)
    {
        terrainType = terraintype;
        value1 = Value1;
        value2 = Value2;
    }
    public double heightFunction(double x)
    {
        double height = 0;
        if(terrainType == type.flat)
        {
            height = value1;
        }
        if(terrainType == type.linear)
        {
            height = x*value1;
        }
        if(terrainType == type.sinusoidal)
        {
            height = value1 * Math.sin(value2 * x);
        }
        return height;
    }
}

