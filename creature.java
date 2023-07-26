import java.awt.*;
import java.util.Random;

public class creature
{
    public double velCap;
    public double groundHeight;
    public boolean[][] nodes;
    public node[][] nodeObjects;
    public muscle[][][] muscleObjects;
    public int numMuscles;
    public int[][] triangles;
    public int numNodes;
    public int length;
    public int height;
    public double nodeDistance;
    public double spawnX;
    public double spawnY;

    public terrain t;

    public neuralNetwork brain;

    public creature(int maxLength, int maxHeight, int numberOfNodes, double distanceBetweenNodes, double spawnx, double spawny, double velocityCap)
    {
        velCap = velocityCap;
        spawnX = spawnx;
        spawnY = spawny;
        length = maxLength;
        height = maxHeight;
        numNodes = numberOfNodes;
        nodeDistance = distanceBetweenNodes;
        Random random = new Random();
        nodes = new boolean[length+1][height+1];
        nodeObjects = new node[length][height];

        for(int x = 0; x < length; x++)
        {
            for(int y = 0; y < height; y++)
            {
                nodes[x][y] = false;
            }
        }
        nodes[(int)(length/2)][(int)(height/2)] = true;
        nodeObjects[(int)(length/2)][(int)(height/2)] = new node(0,spawnY + ((int)(length/2))*distanceBetweenNodes, spawnY + ((int)(height/2))*distanceBetweenNodes,velCap);
        numMuscles = 0;
        for(int i = 0; i < numNodes; i++)
        {
            boolean nodePlaced = false;
            int d = 0;
            while(!nodePlaced)
            {
                d++;
                int xPos = random.nextInt(length);
                int yPos = random.nextInt(height);
                if(!nodes[xPos][yPos])
                {
                    boolean adjacentUp = nodes[xPos][yPos + 1];
                    boolean adjacentRight = nodes[xPos + 1][yPos];
                    boolean adjacentDown;
                    boolean adjacentLeft;

                    if(yPos == 0)
                    {
                        adjacentDown = false;
                    }
                    else
                    {
                        adjacentDown = nodes[xPos][yPos-1];
                    }
                    if(xPos == 0)
                    {
                        adjacentLeft = false;
                    }
                    else
                    {
                        adjacentLeft = nodes[xPos - 1][yPos];
                    }

                    if(adjacentUp || adjacentDown || adjacentLeft || adjacentRight)
                    {
                        nodePlaced = true;
                        nodes[xPos][yPos] = true;
                        nodeObjects[xPos][yPos] = new node(0,spawnX + xPos*nodeDistance, spawnY + yPos*nodeDistance,velCap);

                    }
                }
            }

        }
        muscleObjects = new muscle[length][height][4];
        for(int x = 0; x < length; x++)
        {
            for(int y = 0; y < height; y++)
            {
                if(nodes[x][y])
                {
                    boolean adjacentUp;
                    if(y == height-1)
                    {
                        adjacentUp = false;
                    }
                    else
                    {
                        adjacentUp = nodes[x][y + 1];
                    }

                    boolean adjacentRight;
                    if(x == length-1)
                    {
                        adjacentRight = false;
                    }
                    else
                    {
                        adjacentRight = nodes[x + 1][y];
                    }

                    boolean adjacentUpRight;
                    if(x == length-1 || y == height-1)
                    {
                        adjacentUpRight = false;
                    }
                    else
                    {
                        adjacentUpRight = nodes[x + 1][y + 1];
                    }

                    boolean adjacentUpLeft;
                    if(x == 0 || y == height-1)
                    {
                        adjacentUpLeft = false;
                    }
                    else
                    {
                        adjacentUpLeft = nodes[x - 1][y + 1];
                    }

                    if(adjacentUp)
                    {
                        muscleObjects[x][y][0] = new muscle(nodeObjects[x][y],nodeObjects[x][y+1],0,0);

                        muscleObjects[x][y][0].startX = x;
                        muscleObjects[x][y][0].startY = y;
                        muscleObjects[x][y][0].endX = x;
                        muscleObjects[x][y][0].endY = y+1;
                    }
                    if(adjacentRight)
                    {
                        muscleObjects[x][y][1] = new muscle(nodeObjects[x][y],nodeObjects[x+1][y],0,0);

                        muscleObjects[x][y][1].startX = x;
                        muscleObjects[x][y][1].startY = y;
                        muscleObjects[x][y][1].endX = x+1;
                        muscleObjects[x][y][1].endY = y;
                    }
                    if(adjacentUpRight)
                    {
                        muscleObjects[x][y][2] = new muscle(nodeObjects[x][y],nodeObjects[x+1][y+1],0,0);

                        muscleObjects[x][y][2].startX = x;
                        muscleObjects[x][y][2].startY = y;
                        muscleObjects[x][y][2].endX = x+1;
                        muscleObjects[x][y][2].endY = y+1;
                    }
                    if(adjacentUpLeft)
                    {
                        muscleObjects[x][y][3] = new muscle(nodeObjects[x][y],nodeObjects[x-1][y+1],0,0);

                        muscleObjects[x][y][3].startX = x;
                        muscleObjects[x][y][3].startY = y;
                        muscleObjects[x][y][3].endX = x-1;
                        muscleObjects[x][y][3].endY = y+1;
                    }
                }
            }
        }
        int numTriangles = 0;
        for (int x = 0; x < length; x++)
        {
            for (int y = 0; y < height; y++)
            {
                if(nodes[x][y] && nodes[x][y+1] && nodes[x+1][y])
                {
                    numTriangles = numTriangles + 3;
                }
                if(nodes[x][y] && nodes[x+1][y+1] && nodes[x+1][y])
                {
                    numTriangles = numTriangles + 3;
                }
                if(nodes[x][y] && nodes[x][y+1] && nodes[x+1][y+1])
                {
                    numTriangles = numTriangles + 3;
                }
                if(nodes[x+1][y+1] && nodes[x][y+1] && nodes[x+1][y])
                {
                    numTriangles = numTriangles + 3;
                }
            }
        }
        triangles = new int[numTriangles][2];
        int i = 0;
        for (int x = 0; x < length; x++)
        {
            for (int y = 0; y < height; y++)
            {
                if(nodes[x][y] && nodes[x][y+1] && nodes[x+1][y])
                {
                    triangles[i][0] = x;
                    triangles[i][1] = y;
                    triangles[i+1][0] = x;
                    triangles[i+1][1] = y+1;
                    triangles[i+2][0] = x+1;
                    triangles[i+2][1] = y;
                    i = i + 3;
                }
                if(nodes[x][y] && nodes[x+1][y+1] && nodes[x+1][y])
                {
                    triangles[i][0] = x;
                    triangles[i][1] = y;
                    triangles[i+1][0] = x+1;
                    triangles[i+1][1] = y+1;
                    triangles[i+2][0] = x+1;
                    triangles[i+2][1] = y;
                    i = i + 3;
                }
                if(nodes[x][y] && nodes[x][y+1] && nodes[x+1][y+1])
                {
                    triangles[i][0] = x;
                    triangles[i][1] = y;
                    triangles[i+1][0] = x;
                    triangles[i+1][1] = y+1;
                    triangles[i+2][0] = x+1;
                    triangles[i+2][1] = y+1;
                    i = i + 3;
                }
                if(nodes[x+1][y+1] && nodes[x][y+1] && nodes[x+1][y])
                {
                    triangles[i][0] = x+1;
                    triangles[i][1] = y+1;
                    triangles[i+1][0] = x;
                    triangles[i+1][1] = y+1;
                    triangles[i+2][0] = x+1;
                    triangles[i+2][1] = y;
                    i = i + 3;
                }
            }
        }
    }
    public void returnToStart()
    {
        for(int x = 0; x < length; x++)
        {
            for(int y = 0; y < height; y++)
            {
                if(nodes[x][y])
                {
                    nodeObjects[x][y].x =  spawnX + x*nodeDistance;
                    nodeObjects[x][y].y = spawnY + y*nodeDistance;
                    nodeObjects[x][y].xVelocity = 0;
                    nodeObjects[x][y].yVelocity = 0;
                }
            }
        }
    }
    public void generateNetwork(int neuronsPerMiddleLayer, double weightRange, double biasRange)
    {
        brain = new neuralNetwork(length*height,length*height*4,neuronsPerMiddleLayer,neuronsPerMiddleLayer);
        brain.randomizeWeights(-weightRange, weightRange);
        brain.randomizeBiases(-biasRange, biasRange);
    }
    public void mutateNetwork(double weightChange, double biasChange, double percentageWeights, double percentageBiases)
    {
        brain.mutateWeights(weightChange,percentageWeights);
        brain.mutateBiases(biasChange,percentageBiases);
    }
    public void updateMuscleStates()
    {
        double[] inputVector = new double[length*height*2];
        int i = 0;
        for(int x = 0; x < length; x++)
        {
            for(int y = 0; y < height; y++)
            {
                if(nodes[x][y])
                {
                    inputVector[i] = nodeObjects[x][y].x;
                    inputVector[i + 1] = nodeObjects[x][y].y;
                }
                else
                {
                    inputVector[i] = 0;
                    inputVector[i + 1] = 0;
                }
                i = i + 2;
            }
        }
        double[] outputVector = brain.evaluate(inputVector);
        i = 0;
        for(int x = 0; x < length; x++)
        {
            for(int y = 0; y < height; y++)
            {
                for(int z = 0; z < 4; z++)
                {
                    if(muscleObjects[x][y][z] != null)
                    {

                        if(outputVector[i] < 1.0/3.0)
                        {
                            muscleObjects[x][y][z].relaxed = false;
                            muscleObjects[x][y][z].extending = false;
                        }
                        else if(outputVector[i] > 2.0/3.0)
                        {
                            muscleObjects[x][y][z].relaxed = false;
                            muscleObjects[x][y][z].extending = true;
                        }
                        else
                        {
                            muscleObjects[x][y][z].relaxed = true;
                            muscleObjects[x][y][z].extending = false;
                        }
                    }
                    i++;
                }
            }
        }
    }
    public void randomizeNodes(double minMass, double maxMass)
    {
        Random random = new Random();
        for(int x = 0; x < length; x++)
        {
            for(int y = 0; y < length; y++)
            {
                if(nodes[x][y])
                {
                    nodeObjects[x][y].mass = minMass + (maxMass-minMass)*random.nextDouble();
                }
            }
        }
    }
    public void randomizeMuscles(double minStrength, double maxStrength, double minFlex, double maxFlex)
    {
        Random random = new Random();
        for(int x = 0; x < length; x++)
        {
            for(int y = 0; y < height; y++)
            {
                for(int m = 0; m < 4; m++)
                {
                    if(muscleObjects[x][y][m] != null)
                    {
                        muscleObjects[x][y][m].strengthCoefficient = minStrength + (maxStrength - minStrength) * random.nextDouble();
                        muscleObjects[x][y][m].flexibility = minFlex + (maxFlex - minFlex) * random.nextDouble();
                    }
                }
            }
        }
    }
    public void mutateNodes(double massChange, double percentNodes)
    {
        Random random = new Random();
        for(int x =0; x < length; x++)
        {
            for(int y =0; y < height; y++)
            {
                if(nodes[x][y])
                {
                    if(random.nextDouble()*100 < percentNodes)
                    {
                        if(random.nextInt(2) > 1)
                        {
                            nodeObjects[x][y].mass = nodeObjects[x][y].mass + random.nextDouble()*massChange;
                        }
                        else
                        {
                            nodeObjects[x][y].mass = nodeObjects[x][y].mass - random.nextDouble()*massChange;
                        }
                        nodeObjects[x][y].mass = Math.abs(nodeObjects[x][y].mass);
                    }
                }
            }
        }
    }
    public void mutateMuscles(double strengthChange, double flexChange, double percentMuscles)
    {
        Random random = new Random();
        for(int x =0; x < numMuscles; x++)
        {
            for(int y =0; y < numMuscles; y++)
            {
                for(int m =0; m < numMuscles; m++)
                {
                    if(random.nextDouble()*100 < percentMuscles)
                    {
                        if(random.nextInt(2) == 1)
                        {
                            muscleObjects[x][y][m].strengthCoefficient = muscleObjects[x][y][m].strengthCoefficient + random.nextDouble() * strengthChange;
                        }
                        else
                        {
                            muscleObjects[x][y][m].strengthCoefficient = muscleObjects[x][y][m].strengthCoefficient - random.nextDouble() * strengthChange;
                        }
                        if(random.nextInt(2) == 1)
                        {
                            muscleObjects[x][y][m].flexibility = muscleObjects[x][y][m].flexibility + random.nextDouble() * flexChange;
                        }
                        else
                        {
                            muscleObjects[x][y][m].flexibility = muscleObjects[x][y][m].flexibility - random.nextDouble() * flexChange;
                        }
                        muscleObjects[x][y][m].strengthCoefficient = Math.abs(muscleObjects[x][y][m].strengthCoefficient);
                        muscleObjects[x][y][m].flexibility = Math.abs(muscleObjects[x][y][m].flexibility);
                    }
                }
            }
        }
    }
    public void addOrSubtractNodes(int maxNodesChange, double percentageProbability, double minMass, double maxMass, double minStrength, double maxStrength, double minFlex, double maxFlex)
    {
        Random random = new Random();
        if(random.nextDouble()*100 < percentageProbability)
        {
            int nodesChange = random.nextInt(maxNodesChange);
            for (int i = 0; i < nodesChange; i++)
            {
                if(random.nextInt(2) == 1)
                {
                    numNodes++;
                    boolean nodePlaced = false;
                    while(!nodePlaced)
                    {

                        int xPos = random.nextInt(length);
                        int yPos = random.nextInt(height);
                        if(!nodes[xPos][yPos])
                        {
                            boolean adjacentUp = nodes[xPos][yPos + 1];
                            boolean adjacentRight = nodes[xPos + 1][yPos];
                            boolean adjacentDown;
                            boolean adjacentLeft;

                            if(yPos == 0)
                            {
                                adjacentDown = false;
                            }
                            else
                            {
                                adjacentDown = nodes[xPos][yPos - 1];
                            }
                            if(xPos == 0)
                            {
                                adjacentLeft = false;
                            }
                            else
                            {
                                adjacentLeft = nodes[xPos - 1][yPos];
                            }

                            if(adjacentUp || adjacentDown || adjacentLeft || adjacentRight)
                            {
                                nodePlaced = true;
                                nodes[xPos][yPos] = true;
                                nodeObjects[xPos][yPos] = new node(0,xPos*nodeDistance, groundHeight + yPos*nodeDistance, velCap);
                                nodeObjects[xPos][yPos].mass = minMass + (maxMass-minMass)*random.nextDouble();
                            }
                        }
                    }
                }
                else if(numNodes > 0)
                {
                    numNodes--;
                    boolean nodeSubtracted = false;

                    while(!nodeSubtracted)
                    {
                        int xPos = random.nextInt(length);
                        int yPos = random.nextInt(height);

                        if(nodes[xPos][yPos])
                        {
                            for(int x = 0; x < length; x++)
                            {
                                for(int y = 0; y < height; y++)
                                {
                                    for(int m = 0; m < 4; m++)
                                    {
                                        if(muscleObjects[x][y][m] != null)
                                        {
                                            if(muscleObjects[x][y][m].startNode == nodeObjects[xPos][yPos] || muscleObjects[x][y][m].endNode == nodeObjects[xPos][yPos])
                                            {
                                                muscleObjects[x][y][m] = null;
                                            }
                                        }
                                    }
                                }
                            }
                            nodes[xPos][yPos] = false;
                            nodeObjects[xPos][yPos] = null;
                            nodeSubtracted = true;
                        }
                    }

                }
            }
        }

        for(int x = 0; x < length; x++)
        {
            for(int y = 0; y < height; y++)
            {
                if(!nodes[x][y])
                {
                    continue;
                }
                boolean nodeAdjacentUp;
                if(y == height-1)
                {
                    nodeAdjacentUp = false;
                }
                else
                {
                    nodeAdjacentUp = nodes[x][y + 1];
                }

                boolean nodeAdjacentRight;
                if(x == length-1)
                {
                    nodeAdjacentRight = false;
                }
                else
                {
                    nodeAdjacentRight = nodes[x + 1][y];
                }

                boolean nodeAdjacentUpRight;
                if(x == length-1 || y == height-1)
                {
                    nodeAdjacentUpRight = false;
                }
                else
                {
                    nodeAdjacentUpRight = nodes[x + 1][y + 1];
                }

                boolean nodeAdjacentUpLeft;
                if(x == 0 || y == height-1)
                {
                    nodeAdjacentUpLeft = false;
                }
                else
                {
                    nodeAdjacentUpLeft = nodes[x - 1][y + 1];
                }

                if(nodeAdjacentUp && muscleObjects[x][y][0] == null)
                {

                    muscleObjects[x][y][0] = new muscle(nodeObjects[x][y],nodeObjects[x][y+1],0,0);

                    muscleObjects[x][y][0].startX = x;
                    muscleObjects[x][y][0].startY = y;
                    muscleObjects[x][y][0].endX = x;
                    muscleObjects[x][y][0].endY = y+1;

                    muscleObjects[x][y][0].strengthCoefficient = minStrength + (maxStrength-minStrength)*random.nextDouble();
                    muscleObjects[x][y][0].flexibility = minFlex + (maxFlex-minFlex)*random.nextDouble();
                }
                if(nodeAdjacentRight && muscleObjects[x][y][1] == null)
                {

                    muscleObjects[x][y][1] = new muscle(nodeObjects[x][y],nodeObjects[x+1][y],0,0);

                    muscleObjects[x][y][1].startX = x;
                    muscleObjects[x][y][1].startY = y;
                    muscleObjects[x][y][1].endX = x+1;
                    muscleObjects[x][y][1].endY = y;

                    muscleObjects[x][y][1].strengthCoefficient = minStrength + (maxStrength-minStrength)*random.nextDouble();
                    muscleObjects[x][y][1].flexibility = minFlex + (maxFlex-minFlex)*random.nextDouble();
                }
                if(nodeAdjacentUpRight && muscleObjects[x][y][2] == null)
                {

                    muscleObjects[x][y][2] = new muscle(nodeObjects[x][y],nodeObjects[x+1][y+1],0,0);

                    muscleObjects[x][y][2].startX = x;
                    muscleObjects[x][y][2].startY = y;
                    muscleObjects[x][y][2].endX = x+1;
                    muscleObjects[x][y][2].endY = y+1;

                    muscleObjects[x][y][2].strengthCoefficient = minStrength + (maxStrength-minStrength)*random.nextDouble();
                    muscleObjects[x][y][2].flexibility = minFlex + (maxFlex-minFlex)*random.nextDouble();
                }
                if(nodeAdjacentUpLeft && muscleObjects[x][y][3] == null)
                {

                    muscleObjects[x][y][3] = new muscle(nodeObjects[x][y],nodeObjects[x-1][y+1],0,0);

                    muscleObjects[x][y][3].startX = x;
                    muscleObjects[x][y][3].startY = y;
                    muscleObjects[x][y][3].endX = x-1;
                    muscleObjects[x][y][3].endY = y+1;

                    muscleObjects[x][y][3].strengthCoefficient = minStrength + (maxStrength-minStrength)*random.nextDouble();
                    muscleObjects[x][y][3].flexibility = minFlex + (maxFlex-minFlex)*random.nextDouble();
                }
            }
        }
    }
    public void applyPhysics(double gravityX, double gravityY, double ground, int tick)
    {

        for(int x = 0; x < length; x++)
        {
            for(int y = 0; y < height; y++)
            {
                if(nodes[x][y])
                {
                    nodeObjects[x][y].updatePosition(1, ground, t, tick);
                    nodeObjects[x][y].applyForce(gravityX*nodeObjects[x][y].mass,gravityY*nodeObjects[x][y].mass,1);

                }
            }
        }

        for(int x = 0; x < length; x++)
        {
            for(int y = 0; y < height; y++)
            {
                for(int m = 0; m < 4; m++)
                {
                    if(muscleObjects[x][y][m] != null)
                    {
                        muscleObjects[x][y][m].applyForces(1);
                    }
                }
            }
        }
    }
    public double averageX()
    {
        double mean = 0;
        for(int x = 0; x < length; x++)
        {
            for(int y = 0; y < height; y++)
            {
                if(nodes[x][y])
                {
                    mean = mean + nodeObjects[x][y].x;
                }
            }
        }
        mean = mean/numNodes;
        return mean;
    }
    public double averageNodeMass()
    {
        double mean = 0;
        for(int x = 0; x < length; x++)
        {
            for(int y = 0; y < height; y++)
            {
                if(nodes[x][y])
                {
                    mean = mean + nodeObjects[x][y].mass;
                }
            }
        }
        mean = mean/numNodes;
        return mean;
    }
    public double averageMuscleStrength()
    {
        double mean = 0;
        for(int x = 0; x < length; x++)
        {
            for(int y = 0; y < height; y++)
            {
                for(int m = 0; m < 4; m++)
                {
                    if(muscleObjects[x][y][m] != null)
                    {
                        mean = mean + muscleObjects[x][y][m].strengthCoefficient;
                    }
                }
            }
        }
        mean = mean/numNodes;
        return mean;
    }
    public double averageMuscleFlexibility()
    {
        double mean = 0;
        for(int x = 0; x < length; x++)
        {
            for(int y = 0; y < height; y++)
            {
                for(int m = 0; m < 4; m++)
                {
                    if(muscleObjects[x][y][m] != null)
                    {
                        mean = mean + muscleObjects[x][y][m].flexibility;
                    }
                }
            }
        }
        mean = mean/numNodes;
        return mean;
    }

}
