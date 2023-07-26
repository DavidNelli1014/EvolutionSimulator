

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class app extends Canvas implements Runnable {

    public int numGenerations = 150;
    public int numTrials = 14;
    public int trial = 0;
    public double velocityCap = 9;
    private Thread thread;
    private boolean running = false;
    private double ground = 450;

    public int xOffset = 0;
    public int yOffset = 0;

    public int HEIGHT = 600;
    public int WIDTH = 600;

    public int groundRes = 100;
    public terrain t;

    private double gravityX = 0;
    private double gravityY = 0.04;

    private int maxLength = 20;
    private int maxHeight = 20;
    private int numNodes = 20;
    private double nodeDistance = 30;
    private double spawnX = 0;
    private double spawnY = 0;

    private double minStrength = 0.03;
    private double maxStrength = 0.04;
    private double minFlexibility = 10;
    private double maxFlexibility = 20;

    private double minMass = 0.1;
    private double maxMass = 0.2;

    private int neuronsPerMiddleLayer = 10;
    private double networkWeightRange = 1;
    private double networkBiasRange = 1;

    public terrain.type terrainType = terrain.type.sinusoidal;
    private double terrainValue1 =  50;
    private double terrainValue2 = 0.04;

    private int displayedCreature = 0;
    private int numCreatures = 10;
    private creature[] population;

    private int generation = 0;
    private int ticks = 0;
    private double timePerTrial = 10;
    private boolean trialRunning = false;

    private double eliminationRate = 0.5;

    private double nodeMassMutation = 1;
    private double muscleStrengthMutation = 0.5;
    private double muscleFlexibilityMutation = 0.5;

    private double percentNodesMutate = 5;
    private double percentMusclesMutate = 5;

    private double networkWeightMutation = 0.5;
    private double networkBiasMutation = 0.5;

    private double percentWeightMutation = 15;
    private double percentBiasMutation = 5;

    public File nodeWeights;
    public File muscleStrengths;
    public File muscleFlexibilities;
    public File scores;
    public File nodeCounts;

    public File nodeWeightsSD;
    public File muscleStrengthsSD;
    public File muscleFlexibilitiesSD;
    public File scoresSD;
    public File nodeCountsSD;

    public int maxNodesChange = 10;
    public int nodesChangeProbability = 10;

    public void firstFrame() throws IOException {

        if(trial == 0)
        {
            terrainType = terrain.type.flat;
            terrainValue1 = 0;
            terrainValue2 = 0;
            gravityY = 0.04;
            numNodes = 20;
        }
        else if(trial == 1)
        {
            terrainType = terrain.type.flat;
            terrainValue1 = 0;
            terrainValue2 = 0;
            gravityY = 0.08;
            numNodes = 20;
        }
        else if(trial == 2)
        {
            terrainType = terrain.type.flat;
            terrainValue1 = 0;
            terrainValue2 = 0;
            gravityY = 0.02;
            numNodes = 20;
        }
        else if(trial == 3)
        {
            terrainType = terrain.type.flat;
            terrainValue1 = 0;
            terrainValue2 = 0;
            gravityY = 0.04;
            numNodes = 10;
        }
        else if(trial == 4)
        {
            terrainType = terrain.type.linear;
            terrainValue1 = -0.3;
            terrainValue2 = 0;
            gravityY = 0.04;
            numNodes = 20;
        }
        else if(trial == 5)
        {
            terrainType = terrain.type.linear;
            terrainValue1 = -0.6;
            terrainValue2 = 0;
            gravityY = 0.04;
            numNodes = 20;
        }
        else if(trial == 6)
        {
            terrainType = terrain.type.linear;
            terrainValue1 = -0.3;
            terrainValue2 = 0;
            gravityY = 0.08;
            numNodes = 20;
        }
        else if(trial == 7)
        {
            terrainType = terrain.type.linear;
            terrainValue1 = -0.3;
            terrainValue2 = 0;
            gravityY = 0.02;
            numNodes = 20;
        }
        else if(trial == 8)
        {
            terrainType = terrain.type.linear;
            terrainValue1 = -0.3;
            terrainValue2 = 0;
            gravityY = 0.04;
            numNodes = 10;
        }
        else if(trial == 9)
        {
            terrainType = terrain.type.sinusoidal;
            terrainValue1 = 50;
            terrainValue2 = 0.02;
            gravityY = 0.04;
            numNodes = 20;
        }
        else if(trial == 10)
        {
            terrainType = terrain.type.sinusoidal;
            terrainValue1 = 50;
            terrainValue2 = 0.02;
            gravityY = 0.02;
            numNodes = 20;
        }
        else if(trial == 11)
        {
            terrainType = terrain.type.sinusoidal;
            terrainValue1 = 50;
            terrainValue2 = 0.02;
            gravityY = 0.08;
            numNodes = 20;
        }
        else if(trial == 12)
        {
            terrainType = terrain.type.sinusoidal;
            terrainValue1 = 50;
            terrainValue2 = 0.02;
            gravityY = 0.04;
            numNodes = 10;
        }
        else if(trial == 13)
        {
            terrainType = terrain.type.sinusoidal;
            terrainValue1 = 50;
            terrainValue2 = 0.04;
            gravityY = 0.04;
            numNodes = 20;
        }
        else if(trial == 14)
        {
            this.stop();
        }


        t = new terrain(terrainType, terrainValue1,terrainValue2);
        population = new creature[numCreatures];
        for(int i = 0; i < numCreatures; i++)
        {
            population[i] = new creature(maxLength,maxHeight,numNodes,nodeDistance,spawnX,spawnY, velocityCap);
            population[i].randomizeNodes(minMass,maxMass);
            population[i].randomizeMuscles(minStrength,maxStrength,minFlexibility,maxFlexibility);
            population[i].generateNetwork(neuronsPerMiddleLayer,networkWeightRange,networkBiasRange);
            population[i].t = t;
        }

        nodeWeights = new File("weights"+trial+".txt");
        muscleStrengths = new File("strengths"+trial+".txt");
        muscleFlexibilities = new File("flex"+trial+".txt");
        scores = new File("scores"+trial+".txt");
        nodeCounts = new File("counts"+trial+".txt");

        nodeWeightsSD = new File("weightsSD"+trial+".txt");
        muscleStrengthsSD = new File("strengthsSD"+trial+".txt");
        muscleFlexibilitiesSD = new File("flexSD"+trial+".txt");
        scoresSD = new File("scoresSD"+trial+".txt");
        nodeCountsSD = new File("countsSD"+trial+".txt");

        nodeWeights.createNewFile();
        muscleStrengths.createNewFile();
        muscleFlexibilities.createNewFile();
        scores.createNewFile();
        nodeCounts.createNewFile();

        nodeWeightsSD.createNewFile();
        muscleStrengthsSD.createNewFile();
        muscleFlexibilitiesSD.createNewFile();
        scoresSD.createNewFile();
        nodeCountsSD.createNewFile();

        trialRunning = true;

    }
    public creature copyCreature(creature c)
    {
        creature newCreature = new creature(maxLength,maxHeight,numNodes,nodeDistance,spawnX,spawnY,velocityCap);
        newCreature.t = c.t;
        for(int x = 0; x < maxLength; x++)
        {
            for(int y = 0; y < maxHeight; y++)
            {
                if(c.nodes[x][y])
                {

                    newCreature.nodes[x][y] = true;
                    newCreature.nodeObjects[x][y] = new node(c.nodeObjects[x][y].mass,c.nodeObjects[x][y].startX,c.nodeObjects[x][y].startY,velocityCap);
                }
                else
                {
                    newCreature.nodes[x][y] = false;
                    newCreature.nodeObjects[x][y] = null;
                }
            }
        }
        for(int x = 0; x < maxLength; x++)
        {
            for(int y = 0; y < maxHeight; y++)
            {
                if(c.muscleObjects[x][y][0] != null)
                {
                    newCreature.muscleObjects[x][y][0] = new muscle(newCreature.nodeObjects[x][y],newCreature.nodeObjects[x][y+1],c.muscleObjects[x][y][0].strengthCoefficient,c.muscleObjects[x][y][0].flexibility);
                }
                else
                {
                    newCreature.muscleObjects[x][y][0] = null;
                }
                if(c.muscleObjects[x][y][1] != null)
                {
                    newCreature.muscleObjects[x][y][1] = new muscle(newCreature.nodeObjects[x][y],newCreature.nodeObjects[x+1][y],c.muscleObjects[x][y][1].strengthCoefficient,c.muscleObjects[x][y][1].flexibility);
                }
                else
                {
                    newCreature.muscleObjects[x][y][1] = null;
                }
                if(c.muscleObjects[x][y][2] != null)
                {
                    newCreature.muscleObjects[x][y][2] = new muscle(newCreature.nodeObjects[x][y],newCreature.nodeObjects[x+1][y+1],c.muscleObjects[x][y][2].strengthCoefficient,c.muscleObjects[x][y][2].flexibility);
                }
                else
                {
                    newCreature.muscleObjects[x][y][2] = null;
                }
                if(c.muscleObjects[x][y][3] != null)
                {
                    newCreature.muscleObjects[x][y][3] = new muscle(newCreature.nodeObjects[x][y],newCreature.nodeObjects[x-1][y+1],c.muscleObjects[x][y][3].strengthCoefficient,c.muscleObjects[x][y][3].flexibility);
                }
                else
                {
                    newCreature.muscleObjects[x][y][3] = null;
                }
            }
        }
        newCreature.generateNetwork(neuronsPerMiddleLayer,networkWeightRange,networkBiasRange);
        Random random = new Random();
        for (int x = 0; x < c.brain.weightLayer1.length; x++)
        {
            for(int y = 0; y < c.brain.weightLayer1[x].length; y++)
            {
                newCreature.brain.weightLayer1[x][y] = c.brain.weightLayer1[x][y];
            }
        }
        for (int x = 0; x < c.brain.weightLayer2.length; x++)
        {
            for(int y = 0; y < c.brain.weightLayer2[x].length; y++)
            {
                newCreature.brain.weightLayer2[x][y] = c.brain.weightLayer2[x][y];
            }
        }
        for (int x = 0; x < c.brain.weightLayer3.length; x++)
        {
            for(int y = 0; y < c.brain.weightLayer3[x].length; y++)
            {
                newCreature.brain.weightLayer3[x][y] = c.brain.weightLayer3[x][y];
            }
        }
        for(int i = 0; i < c.brain.biasLayer1.length; i++)
        {
            newCreature.brain.biasLayer1[i] = c.brain.biasLayer1[i];
        }
        for(int i = 0; i < c.brain.biasLayer2.length; i++)
        {
            newCreature.brain.biasLayer2[i] = c.brain.biasLayer2[i];
        }
        for(int i = 0; i < c.brain.biasLayer3.length; i++)
        {
            newCreature.brain.biasLayer3[i] = c.brain.biasLayer3[i];
        }
        return newCreature;
    }
    public app()
    {
        new window(WIDTH,HEIGHT,"New Window",this);
    }
    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }
    public synchronized void stop()
    {
        try
        {
            thread.join();
            running = false;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void run()
    {

        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;

        try
        {
            firstFrame();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        while(running)
        {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1)
            {
                try
                {
                    tick();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                delta--;
            }
            if(running)
            {
                render();
            }
            frames++;
            if(System.currentTimeMillis() - timer > 1000)
            {
                timer += 1000;
                System.out.println("FPS: " + frames);
                frames = 0;
            }

        }
        stop();
    }
    public static void main(String[] args)
    {
        new app();
    }
    private void tick() throws IOException {
        if(trialRunning)
        {
            if(timePerTrial < ticks/60.0)
            {
                trialRunning = false;
            }
            else
            {
                ticks++;
                for (int i = 0; i < numCreatures; i++)
                {
                    population[i].updateMuscleStates();
                    population[i].applyPhysics(gravityX,gravityY,ground,ticks);
                }
            }

        }
        else
        {
            FileWriter nodeWeightsWriter = new FileWriter(nodeWeights,true);
            FileWriter muscleStrengthsWriter = new FileWriter(muscleStrengths,true);
            FileWriter  muscleFlexibilitiesWriter = new FileWriter(muscleFlexibilities,true);
            FileWriter  scoresWriter = new FileWriter(scores,true);
            FileWriter nodeCountWriter = new FileWriter(nodeCounts,true);

            FileWriter nodeWeightsWriterSD = new FileWriter(nodeWeightsSD,true);
            FileWriter muscleStrengthsWriterSD = new FileWriter(muscleStrengthsSD,true);
            FileWriter  muscleFlexibilitiesWriterSD = new FileWriter(muscleFlexibilitiesSD,true);
            FileWriter  scoresWriterSD = new FileWriter(scoresSD,true);
            FileWriter nodeCountWriterSD = new FileWriter(nodeCountsSD,true);

            double meanMass = 0;
            double meanStrength = 0;
            double meanFlex = 0;
            double meanCount = 0;

            for(int i = 0; i < numCreatures; i++)
            {
                meanMass = meanMass + population[i].averageNodeMass();
                meanStrength = meanStrength + population[i].averageMuscleStrength();
                meanFlex = meanFlex + population[i].averageMuscleFlexibility();
                meanCount = meanCount + population[i].numNodes;
            }

            meanMass = meanMass / numCreatures;
            meanStrength = meanStrength / numCreatures;
            meanFlex = meanFlex / numCreatures;
            meanCount = meanCount / numCreatures;

            nodeWeightsWriter.write("\n" + String.valueOf(meanMass));
            muscleStrengthsWriter.write("\n" + String.valueOf(meanStrength));
            muscleFlexibilitiesWriter.write("\n" + String.valueOf(meanFlex));
            nodeCountWriter.write("\n" + String.valueOf(meanCount));

            nodeWeightsWriter.close();
            muscleStrengthsWriter.close();
            muscleFlexibilitiesWriter.close();
            nodeCountWriter.close();

            double SDMass = 0;
            double SDStrength = 0;
            double SDFlex = 0;
            double SDCount = 0;

            for(int i = 0; i < numCreatures; i++)
            {
                SDMass = SDMass + Math.pow((population[i].averageNodeMass()-meanMass),2);
                SDStrength = SDStrength + Math.pow((population[i].averageMuscleStrength()-meanStrength),2);
                SDFlex = SDFlex + Math.pow((population[i].averageMuscleFlexibility()-meanFlex),2);
                SDCount = SDCount + Math.pow((population[i].numNodes-meanCount),2);
            }

            SDMass =  Math.sqrt(SDMass / (numCreatures - 1));
            SDStrength = Math.sqrt(SDStrength / (numCreatures - 1));
            SDFlex = Math.sqrt(SDFlex / (numCreatures - 1));
            SDCount = Math.sqrt(SDCount / (numCreatures - 1));

            nodeWeightsWriterSD.write("\n" + String.valueOf(SDMass));
            muscleStrengthsWriterSD.write("\n" + String.valueOf(SDStrength));
            muscleFlexibilitiesWriterSD.write("\n" + String.valueOf(SDFlex));
            nodeCountWriterSD.write("\n" + String.valueOf(SDCount));

            nodeWeightsWriterSD.close();
            muscleStrengthsWriterSD.close();
            muscleFlexibilitiesWriterSD.close();
            nodeCountWriterSD.close();

            double[] scores = new double[numCreatures];
            double meanScore = 0;
            for(int i = 0; i < numCreatures; i++)
            {
                scores[i] = population[i].averageX();

                if(Double.isNaN(scores[i]))
                {
                    scores[i] = 0;
                }
                else{
                    meanScore = meanScore + scores[i];
                }
                population[i].returnToStart();
            }

            meanScore = meanScore / numCreatures;
            scoresWriter.write("\n" + String.valueOf(meanScore));
            scoresWriter.close();

            double SDScore = 0;
            for(int i = 0; i < numCreatures; i++)
            {
                SDScore = SDScore + Math.pow(scores[i]-meanScore,2);
            }
            SDScore =  Math.sqrt(SDScore / (numCreatures - 1));
            scoresWriterSD.write("\n" + String.valueOf(SDScore));
            scoresWriterSD.close();

            double[] sortedScores = new double[numCreatures];
            for(int i = 0; i < scores.length; i++)
            {
                sortedScores[i] = scores[i];
            }
            java.util.Arrays.sort(sortedScores);


            double scoreToBeat = sortedScores[(int)((numCreatures-1)*eliminationRate)];
            int bestIndex = 0;
            for(int i = 0; i < numCreatures; i++)
            {
                bestIndex = i;
                if(scores[i] == sortedScores[numCreatures-1])
                {
                    break;
                }
            }
            displayedCreature = bestIndex;
            for(int i = 0; i < numCreatures; i++)
            {
                if(scores[i] < scoreToBeat)
                {
                    Random r = new Random();
                    boolean foundSurvivor = false;
                    while(!foundSurvivor)
                    {
                        int sIndex = r.nextInt(numCreatures);
                        creature survivor = population[sIndex];
                        if(scores[sIndex] >= scoreToBeat)
                        {
                            population[i] = copyCreature(population[sIndex]);
                            population[i].mutateNodes(nodeMassMutation,percentNodesMutate);
                            population[i].mutateMuscles(muscleStrengthMutation,muscleFlexibilityMutation,percentMusclesMutate);
                            population[i].addOrSubtractNodes(maxNodesChange,nodesChangeProbability,minMass,maxMass,minStrength,maxStrength,minFlexibility,maxFlexibility);
                            population[i].mutateNetwork(networkWeightMutation,networkBiasMutation,percentWeightMutation,percentBiasMutation);

                            foundSurvivor = true;
                        }
                    }
                }
            }
            trialRunning = true;
            ticks = 0;
            generation++;
            if(generation > numGenerations)
            {
                generation = 0;
                trial++;
                trialRunning = false;
                firstFrame();
            }
            System.out.println("Generation "+generation);
        }
    }
    private void render()
    {

        xOffset = -(int)population[displayedCreature].averageX() + WIDTH/2;
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null)
        {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        g.clearRect(0,0,WIDTH,HEIGHT);
        for(int x = 0; x < population[displayedCreature].length; x++)
        {
            for(int y = 0; y < population[displayedCreature].height; y++)
            {
                if(population[displayedCreature].nodes[x][y])
                {
                    g.setColor(Color.BLUE);
                    g.fillOval((int)population[displayedCreature].nodeObjects[x][y].x - 3 + xOffset,(int)population[displayedCreature].nodeObjects[x][y].y - 3 + yOffset,6,6);
                }
            }
        }

        for(int x = 0; x < population[displayedCreature].length; x++)
        {
            for(int y = 0; y < population[displayedCreature].height; y++)
            {
                for(int m = 0; m < 4; m++)
                {
                    if(population[displayedCreature].muscleObjects[x][y][m] != null)
                    {
                        g.setColor(Color.BLACK);
                        g.drawLine((int)population[displayedCreature].muscleObjects[x][y][m].startNode.x + xOffset,
                                (int)population[displayedCreature].muscleObjects[x][y][m].startNode.y + yOffset,
                                (int)population[displayedCreature].muscleObjects[x][y][m].endNode.x + xOffset,
                                (int)population[displayedCreature].muscleObjects[x][y][m].endNode.y + yOffset);
                    }
                }
            }
        }

        int[] groundPoints = new int[groundRes*2];
        for(int i = 0; i < groundRes*2; i++)
        {
            groundPoints[i] =  (int)ground + (int)t.heightFunction((i * WIDTH/ groundRes));
        }
        for(int i = 0; i < groundRes*2-1; i++)
        {
            g.drawLine((i * WIDTH / groundRes)+ xOffset, groundPoints[i] + yOffset,((i+1)* WIDTH / groundRes)+ xOffset, groundPoints[i+1] + yOffset);
        }
        g.dispose();
        bs.show();
    }
}
