import java.util.Random;

public class neuralNetwork
{

    public double[][] weightLayer1;
    public double[][] weightLayer2;
    public double[][] weightLayer3;

    public double[] biasLayer1;
    public double[] biasLayer2;
    public double[] biasLayer3;

    public int inputSize;
    public int layerOneSize;
    public int layerTwoSize;
    public int outputSize;

    public neuralNetwork(int numInputs, int numOutputs, int middleLayerOneSize, int middleLayerTwoSize)
    {
        inputSize = numInputs;
        layerOneSize = middleLayerOneSize;
        layerTwoSize = middleLayerTwoSize;
        outputSize = numOutputs;

        weightLayer1 = new double[numInputs][middleLayerOneSize];
        weightLayer2 = new double[middleLayerOneSize][middleLayerTwoSize];
        weightLayer3 = new double[middleLayerTwoSize][numOutputs];

        biasLayer1 = new double[middleLayerOneSize];
        biasLayer2 = new double[middleLayerTwoSize];
        biasLayer3 = new double[numOutputs];
    }
    public void randomizeWeights(double minWeight, double maxWeight)
    {
        Random random = new Random();
        for (int x = 0; x < weightLayer1.length; x++)
        {
            for(int y = 0; y < weightLayer1[x].length; y++)
            {
                weightLayer1[x][y] = (random.nextDouble() * (maxWeight-minWeight)) + minWeight;
            }
        }
        for (int x = 0; x < weightLayer2.length; x++)
        {
            for(int y = 0; y < weightLayer2[x].length; y++)
            {
                weightLayer2[x][y] = (random.nextDouble() * (maxWeight-minWeight)) + minWeight;
            }
        }
        for (int x = 0; x < weightLayer3.length; x++)
        {
            for(int y = 0; y < weightLayer3[x].length; y++)
            {
                weightLayer3[x][y] = (random.nextDouble() * (maxWeight-minWeight)) + minWeight;
            }
        }
    }
    public void randomizeBiases(double minBias, double maxBias)
    {
        Random random = new Random();
        for(int i = 0; i < biasLayer1.length; i++)
        {
            biasLayer1[i] = (random.nextDouble()*(maxBias-minBias)) + minBias;
        }
        for(int i = 0; i < biasLayer2.length; i++)
        {
            biasLayer2[i] = (random.nextDouble()*(maxBias-minBias)) + minBias;
        }
        for(int i = 0; i < biasLayer3.length; i++)
        {
            biasLayer3[i] = (random.nextDouble()*(maxBias-minBias)) + minBias;
        }
    }
    public double[] evaluate(double[] inputs)
    {
        double[] layer1 = new double[layerOneSize];
        double[] layer2 = new double[layerTwoSize];
        double[] output = new double[outputSize];

        for(int i = 0; i < layerOneSize; i++)
        {
            layer1[i] = 0;
            for(int n = 0; n < inputSize; n++)
            {
                layer1[i] = layer1[i] + weightLayer1[n][i]*inputs[n] + biasLayer1[i];
                layer1[i] = sigmoid(layer1[i]);
            }

        }
        for(int i = 0; i < layerTwoSize; i++)
        {
            layer2[i] = 0;
            for(int n = 0; n < layerOneSize; n++)
            {
                layer2[i] = layer2[i] + weightLayer2[n][i]*layer1[n] + biasLayer2[i];
                layer2[i] = sigmoid(layer2[i]);
            }
        }
        for(int i = 0; i < outputSize; i++)
        {
            output[i] = 0;
            for(int n = 0; n < layerTwoSize; n++)
            {
                output[i] = output[i] + weightLayer3[n][i]*layer2[n] + biasLayer3[i];
                output[i] = sigmoid(output[i]);
            }
        }

        return output;

    }
    public void mutateWeights(double weightChange, double percentWeights)
    {
        Random random = new Random();
        for(int x =0; x < weightLayer1.length; x++)
        {
            for(int y =0; y < weightLayer1[x].length; y++)
            {
                    if(random.nextDouble()*100 < percentWeights)
                    {
                        if(random.nextBoolean())
                        {
                            weightLayer1[x][y] = weightLayer1[x][y] + random.nextDouble()*weightChange;
                        }
                        else
                        {
                            weightLayer1[x][y] = weightLayer1[x][y] - random.nextDouble()*weightChange;
                        }
                        weightLayer1[x][y] = Math.abs(weightLayer1[x][y]);
                    }
            }
        }

        for(int x =0; x < weightLayer2.length; x++)
        {
            for(int y =0; y < weightLayer2[x].length; y++)
            {
                if(random.nextDouble()*100 < percentWeights)
                {
                    if(random.nextBoolean())
                    {
                        weightLayer2[x][y] = weightLayer2[x][y] + random.nextDouble()*weightChange;
                    }
                    else
                    {
                        weightLayer2[x][y] = weightLayer2[x][y] - random.nextDouble()*weightChange;
                    }
                    weightLayer2[x][y] = Math.abs(weightLayer2[x][y]);
                }
            }
        }

        for(int x =0; x < weightLayer3.length; x++)
        {
            for(int y =0; y < weightLayer3[x].length; y++)
            {
                if(random.nextDouble()*100 < percentWeights)
                {
                    if(random.nextBoolean())
                    {
                        weightLayer3[x][y] = weightLayer3[x][y] + random.nextDouble()*weightChange;
                    }
                    else
                    {
                        weightLayer3[x][y] = weightLayer3[x][y] - random.nextDouble()*weightChange;
                    }
                    weightLayer3[x][y] = Math.abs(weightLayer3[x][y]);
                }
            }
        }
    }
    public void mutateBiases(double biasChange, double percentBiases)
    {
        Random random = new Random();
        for(int x =0; x < biasLayer1.length; x++)
        {
                if(random.nextDouble()*100 < percentBiases)
                {
                    if(random.nextBoolean())
                    {
                        biasLayer1[x] = biasLayer1[x] + random.nextDouble()*biasChange;
                    }
                    else
                    {
                        biasLayer1[x] = biasLayer1[x] - random.nextDouble()*biasChange;
                    }
                    biasLayer1[x] = Math.abs(biasLayer1[x]);
                }

        }
    }
    private double sigmoid(double x)
    {
        double output = 1 / (1 + Math.exp(-x));
        return output;
    }
}
