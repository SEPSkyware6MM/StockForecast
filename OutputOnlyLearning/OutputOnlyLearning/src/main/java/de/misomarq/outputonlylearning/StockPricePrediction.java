package de.misomarq.outputonlylearning;

/**
 *
 * @author dmarquant
 */
public class StockPricePrediction {
    public static void main(String []args) {
        // Load the training data
        FileIO inFile = new FileIO("../training_data.csv");
        FileIO testFile = new FileIO("../test_data.csv");
        
        //----------------------------------------------------------------------------------------------
        // --- read input File -------------------------------------------------------------------------
        // ---------------------------------------------------------------------------------------------
        int inputNeuronNum = (int) inFile.readSingleValue();		// first line defines number of input neurons
        int hiddenNeuronNum = (int) inFile.readSingleValue();

        inFile.readTable(inputNeuronNum + 2, hiddenNeuronNum); // 30 values in up to 1000 lines
        int outputNeuronNum = inFile.maxCol - inputNeuronNum;

        int MDims = inputNeuronNum+hiddenNeuronNum; // output not included ...

        // -------------------------------------------------------------------------------------------------------
        // ------------------------ calculate least Squares Optimum for Hidden Neuron ----------------------------
        // -------------------------------------------------------------------------------------------------------


        Network net = new Network(inputNeuronNum, hiddenNeuronNum + outputNeuronNum);

        double inVector[] = new  double[MDims+10];
        // ---
        double squareError=0;
        // ############################################################################################
        // ##### random structure #####################################################################
        // ############################################################################################
        for (int t=0;t<hiddenNeuronNum;t++){
                for (int i=0;i<t+inputNeuronNum;i++){
                        net.neuron[t].weight[i]=Math.random()*2.0-1.0;//(rand()%2000)/1000.0-1.0;
                }
        }
        //---- output neuron -------------------------------------------------------------------------------
        EquationSolver equ;
        equ = new EquationSolver(inputNeuronNum + hiddenNeuronNum);	
        for (int time=0; time<inFile.maxRow; time++){
                for (int t=0;t<MDims;t++){
                        inVector[t]=0;
                }
                for (int t=0;t<inputNeuronNum;t++){    // First input values
                        inVector[t] = inFile.value[t][time];
                }
                net.activate(inVector);
                for (int t=0;t<hiddenNeuronNum; t++){
                        inVector[inputNeuronNum+t] = net.neuron[t].output;
                }
                double targetForOutput      = inFile.value[inputNeuronNum][time];
                double activityError        = net.invThreshFunction(targetForOutput);
                equ.leastSquaresAdd(inVector, activityError);

        }

        equ.Solve();

        for (int t=0;t<inputNeuronNum+hiddenNeuronNum;t++){
                //double debugg= equ->solution[t];
                net.neuron[hiddenNeuronNum].weight[t] = equ.solution[t];	// weight from Neuron t to output neuron outnum
        }
        // --- end outputneuron --------------------------------------------------------------------------------	

        int testInputsize = (int)testFile.readSingleValue();
        int testCases = (int)testFile.readSingleValue();
        
        testFile.readTable(testInputsize + 2, testCases);
        int counter = 0;
        for (int j = 0; j < testCases; ++j) {
            double []testVector = new double[testInputsize + 2];
            for (int i = 0; i < testInputsize + 2; ++i) {
                testVector[i] = testFile.value[i][j];
            }
            net.activate(testVector);
            double prediction = net.neuron[hiddenNeuronNum].output;

            if(prediction  > 0 && testVector[testInputsize+1] > 0)
            {
                counter++;
            }
            else if(prediction  < 0 && testVector[testInputsize+1] < 0)
            {
                counter++;
            }
                
            System.out.println("Predicted was: " + prediction);
            System.out.println("Actual was: " + testVector[testInputsize+1]);
        }
        
        System.out.println("Done!");
        System.out.println(counter +  " von " + testCases);
       //net.activate(inVector);
    }
}
