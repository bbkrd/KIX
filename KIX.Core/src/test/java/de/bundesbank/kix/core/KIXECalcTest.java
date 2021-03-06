/*
 * Copyright 2016 Deutsche Bundesbank
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved
 * by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package de.bundesbank.kix.core;

import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.timeseries.simplets.TsFrequency;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;

/**
 *
 * @author Thomas Witthohn
 */
public class KIXECalcTest {

    final double DELTA = 0.0005;

    @Test
    public void TestUnchainQuarterlyLastQuarter() {
        double[] data = {75.1, 78.1, 79.9, 79.7, 81.3, 86.1, 89.2, 90.8, 92.4, 95.6, 98.8, 103.5, 102.0,
                         107.4, 110.6, 113.9, 117.5, 121.1, 124.8, 128.4, 132.4, 136.2, 140.6, 144.7, 149.0};
        TsData a = new TsData(TsFrequency.Quarterly, 2007, 3, data, true);

        TsData result = KIXECalc.unchain(a);

        double[] expResultData = {100, 103.99467, 106.39148, 106.12517, 108.25566, 105.90406, 109.71710, 111.68512, 113.65314,
                                  103.46320, 106.92641, 112.01299, 110.38961, 105.29412, 108.43137, 111.66667, 115.19608,
                                  103.06383, 106.21277, 109.27660, 112.68085, 102.87009, 106.19335, 109.29003, 112.53776};
        assertEquals(expResultData, result.internalStorage());
    }

    @Test
    public void TestUnchainQuarterlyFirstQuarter() {
        double[] data = {78.1, 79.9, 79.7, 81.3, 86.1, 89.2, 90.8, 92.4, 95.6, 98.8, 103.5, 102.0,
                         107.4, 110.6, 113.9, 117.5, 121.1, 124.8, 128.4, 132.4, 136.2, 140.6, 144.7, 149.0};
        TsData a = new TsData(TsFrequency.Quarterly, 2008, 0, data, true);

        TsData result = KIXECalc.unchain(a);

        double[] expResultData = {96.064, 98.278, 98.032, 100, 105.90406, 109.71710, 111.68512, 113.65314, 103.46320, 106.92641, 112.01299, 110.38961, 105.29412, 108.43137,
                                  111.66667, 115.19608, 103.06383, 106.21277, 109.27660, 112.68085, 102.87009, 106.19335, 109.29003, 112.53776};
        assertEquals(expResultData, result.internalStorage());
    }

    @Test
    public void TestAddToWeightSum() {
        double[] data1 = {103.99467, 106.39148, 106.12517, 108.25566, 105.90406, 109.71710, 111.68512, 113.65314,
                          103.46320, 106.92641, 112.01299, 110.38961, 105.29412, 108.43137, 111.66667, 115.19608,
                          103.06383, 106.21277, 109.27660, 112.68085, 102.87009, 106.19335, 109.29003, 112.53776};
        TsData a = new TsData(TsFrequency.Quarterly, 2008, 0, data1, true);

        double[] weight1 = {455.0, 443.0, 431.0, 419.0, 408.0, 380.0, 352.0, 324.0, 290.0, 313.0, 336.0, 359.0,
                            395.6, 387.0, 378.4, 369.8, 368.5, 350.0, 331.5, 313.0, 290.9, 311.8, 306.8, 301.8};
        TsData wa = new TsData(TsFrequency.Quarterly, 2007, 3, weight1, true);

        double[] data2 = {99.64912, 99.82456, 98.07018, 96.66667, 98.45735, 96.91470, 95.37205, 93.82940, 99.12959,
                          98.35590, 95.26112, 94.10058, 100.30832, 99.48613, 98.45838, 97.22508, 99.26004, 98.20296,
                          97.14588, 95.98309, 99.33921, 98.34802, 97.24670, 97.02643};
        TsData b = new TsData(TsFrequency.Quarterly, 2008, 0, data2, true);

        double[] weight2 = {176.0, 181.0, 186.0, 191.0, 195.0, 230.0, 265.0, 300.0, 366.0, 380.0, 394.0, 408.0,
                            430.5, 463.0, 495.5, 528.0, 558.0, 576.0, 594.0, 612.0, 648.6, 672.3, 698.0, 723.7};
        TsData wb = new TsData(TsFrequency.Quarterly, 2007, 3, weight2, true);

        TsData result = KIXECalc.addToWeightSum(a, wa, b, wb);

        double[] expResultData = {102.78260, 104.55982, 103.87845, 105.02323, 103.49592, 105.57702, 106.40975, 107.24248,
                                  101.04537, 102.14469, 102.66667, 101.30152, 102.69590, 103.76980, 104.78352, 105.83097,
                                  100.77294, 101.38873, 101.97067, 102.62435, 100.43248, 100.77719, 100.97571, 101.82925};
        assertEquals(expResultData, result.internalStorage());

    }

    @Test
    public void TestSubtractFromWeightSum() {
        double[] data1 = {103.99467, 106.39148, 106.12517, 108.25566, 105.90406, 109.71710, 111.68512, 113.65314,
                          103.46320, 106.92641, 112.01299, 110.38961, 105.29412, 108.43137, 111.66667, 115.19608,
                          103.06383, 106.21277, 109.27660, 112.68085, 102.87009, 106.19335, 109.29003, 112.53776};
        TsData a = new TsData(TsFrequency.Quarterly, 2008, 0, data1, true);

        double[] weight1 = {455.0, 443.0, 431.0, 419.0, 408.0, 380.0, 352.0, 324.0, 290.0, 313.0, 336.0, 359.0,
                            395.6, 387.0, 378.4, 369.8, 368.5, 350.0, 331.5, 313.0, 290.9, 311.8, 306.8, 301.8};
        TsData wa = new TsData(TsFrequency.Quarterly, 2007, 3, weight1, true);

        double[] data2 = {99.64912, 99.82456, 98.07018, 96.66667, 98.45735, 96.91470, 95.37205, 93.82940, 99.12959,
                          98.35590, 95.26112, 94.10058, 100.30832, 99.48613, 98.45838, 97.22508, 99.26004, 98.20296,
                          97.14588, 95.98309, 99.33921, 98.34802, 97.24670, 97.02643};
        TsData b = new TsData(TsFrequency.Quarterly, 2008, 0, data2, true);

        double[] weight2 = {176.0, 181.0, 186.0, 191.0, 195.0, 230.0, 265.0, 300.0, 366.0, 380.0, 394.0, 408.0,
                            430.5, 463.0, 495.5, 528.0, 558.0, 576.0, 594.0, 612.0, 648.6, 672.3, 698.0, 723.7};
        TsData wb = new TsData(TsFrequency.Quarterly, 2007, 3, weight2, true);

        TsData result = KIXECalc.subtractFromWeightSum(a, wa, b, wb);

        double[] expResultData = {106.73595, 110.53405, 111.20645, 115.56628, 112.72147, 121.43760, 126.61961, 131.80163,
                                  82.59345, 65.65265, 31.33953, 31.94507, 43.79315, -1.91043, -51.26081, -106.48060,
                                  91.86323, 82.62716, 73.55659, 63.51276, 96.46771, 91.96779, 87.45244, 84.41182};
        assertEquals(expResultData, result.internalStorage());

    }

    @Test
    public void TestCombiningUnchainAndAddToWeightSum() {
        double[] data1 = {75.1, 78.1, 79.9, 79.7, 81.3, 86.1, 89.2, 90.8, 92.4, 95.6, 98.8, 103.5, 102.0,
                          107.4, 110.6, 113.9, 117.5, 121.1, 124.8, 128.4, 132.4, 136.2, 140.6, 144.7, 149.0};
        TsData a = new TsData(TsFrequency.Quarterly, 2007, 3, data1, true);

        double[] weight1 = {455.0, 443.0, 431.0, 419.0, 408.0, 380.0, 352.0, 324.0, 290.0, 313.0, 336.0, 359.0,
                            395.6, 387.0, 378.4, 369.8, 368.5, 350.0, 331.5, 313.0, 290.9, 311.8, 306.8, 301.8};
        TsData wA = new TsData(TsFrequency.Quarterly, 2007, 3, weight1, true);

        double[] data2 = {114.0, 113.6, 113.8, 111.8, 110.2, 108.5, 106.8, 105.1, 103.4, 102.5, 101.7, 98.5, 97.3,
                          97.6, 96.8, 95.8, 94.6, 93.9, 92.9, 91.9, 90.8, 90.2, 89.3, 88.3, 88.1};
        TsData b = new TsData(TsFrequency.Quarterly, 2007, 3, data2, true);

        double[] weight2 = {176.0, 181.0, 186.0, 191.0, 195.0, 230.0, 265.0, 300.0, 366.0, 380.0, 394.0, 408.0,
                            430.5, 463.0, 495.5, 528.0, 558.0, 576.0, 594.0, 612.0, 648.6, 672.3, 698.0, 723.7};
        TsData wB = new TsData(TsFrequency.Quarterly, 2007, 3, weight2, true);

        TsData result = KIXECalc.addToWeightSum(KIXECalc.unchain(a), wA, KIXECalc.unchain(b), wB);

        double[] expResultData = {100, 102.78260, 104.55982, 103.87845, 105.02323, 103.49592, 105.57702, 106.40975, 107.24248,
                                  101.04537, 102.14469, 102.66667, 101.30152, 102.69590, 103.76980, 104.78352, 105.83097,
                                  100.77294, 101.38873, 101.97067, 102.62435, 100.43248, 100.77719, 100.97571, 101.82925};
        assertEquals(expResultData, result.internalStorage());
    }

    @Test
    public void TestCombiningUnchainAndAddToWeightSum2() {
        double[] data1 = {75.1, 78.1, 79.9, 79.7, 81.3, 86.1, 89.2, 90.8, 92.4, 95.6, 98.8, 103.5, 102.0,
                          107.4, 110.6, 113.9, 117.5, 121.1, 124.8, 128.4, 132.4, 136.2, 140.6, 144.7, 149.0};
        TsData a = new TsData(TsFrequency.Quarterly, 2007, 3, data1, true);

        double[] weight1 = {455.0, 443.0, 431.0, 419.0, 408.0, 380.0, 352.0, 324.0, 290.0, 313.0, 336.0, 359.0,
                            395.6, 387.0, 378.4, 369.8, 368.5, 350.0, 331.5, 313.0, 290.9, 311.8, 306.8, 301.8};
        TsData wA = new TsData(TsFrequency.Quarterly, 2007, 3, weight1, true);

        double[] data2 = {114.0, 113.6, 113.8, 111.8, 110.2, 108.5, 106.8, 105.1, 103.4, 102.5, 101.7, 98.5, 97.3,
                          97.6, 96.8, 95.8, 94.6, 93.9, 92.9, 91.9, 90.8, 90.2, 89.3, 88.3, 88.1};
        TsData b = new TsData(TsFrequency.Quarterly, 2008, 3, data2, true);

        double[] weight2 = {176.0, 181.0, 186.0, 191.0, 195.0, 230.0, 265.0, 300.0, 366.0, 380.0, 394.0, 408.0,
                            430.5, 463.0, 495.5, 528.0, 558.0, 576.0, 594.0, 612.0, 648.6, 672.3, 698.0, 723.7};
        TsData wB = new TsData(TsFrequency.Quarterly, 2008, 3, weight2, true);

        TsData result = KIXECalc.addToWeightSum(KIXECalc.unchain(a), wA, KIXECalc.unchain(b), wB);

        double[] expResultData = {105.953, 104.01901, 106.735785, 107.581984, 108.533926, 101.450541, 102.901082, 105.322301,
                                  103.731382, 102.33165, 103.58943, 103.782699, 105.058273, 101.579168, 102.588462,
                                  103.447755, 104.353303, 100.497129, 100.941098, 101.307421, 101.656023};
        assertEquals(expResultData, result.internalStorage());
    }

    @Test
    public void TestChain() {
        double[] dataWeightSum = {102.78260, 104.55982, 103.87845, 105.02323, 103.49592, 105.57702, 106.40975, 107.24248,
                                  101.04537, 102.14469, 102.66667, 101.30152, 102.69590, 103.76980, 104.78352, 105.83097,
                                  100.77294, 101.38873, 101.97067, 102.62435, 100.43248, 100.77719, 100.97571, 101.82925};
        TsData weightSum = new TsData(TsFrequency.Quarterly, 2008, 0, dataWeightSum, true);

        TsData result = KIXECalc.chain(weightSum);

        double[] expResultData = {102.78260, 104.55982, 103.87845, 105.02323, 108.69476, 110.88040, 111.75495, 112.62951,
                                  113.80690, 115.04507, 115.63297, 114.09541, 117.17132, 118.39658, 119.55319, 120.74828,
                                  121.68159, 122.42515, 123.12783, 123.91713, 124.45306, 124.88021, 125.12620, 126.18389};

        assertEquals(expResultData, result.internalStorage());
    }

    @Test
    public void TestChainNoFullLastYear() {
        double[] dataWeightSum = {102.78260, 104.55982, 103.87845, 105.02323, 103.49592, 105.57702, 106.40975, 107.24248,
                                  101.04537, 102.14469, 102.66667, 101.30152, 102.69590, 103.76980, 104.78352, 105.83097,
                                  100.77294, 101.38873, 101.97067, 102.62435, 100.43248, 100.77719, 100.97571};
        TsData weightSum = new TsData(TsFrequency.Quarterly, 2008, 0, dataWeightSum, true);

        TsData result = KIXECalc.chain(weightSum);

        double[] expResultData = {102.78260, 104.55982, 103.87845, 105.02323, 108.69476, 110.88040, 111.75495, 112.62951,
                                  113.80690, 115.04507, 115.63297, 114.09541, 117.17132, 118.39658, 119.55319, 120.74828,
                                  121.68159, 122.42515, 123.12783, 123.91713, 124.45306, 124.88021, 125.12620};

        assertEquals(expResultData, result.internalStorage());
    }

    @Test
    public void TestWeightInRefYear() {
        double[] data1 = {75.1, 78.1, 79.9, 79.7, 81.3, 86.1, 89.2, 90.8, 92.4, 95.6, 98.8, 103.5, 102.0,
                          107.4, 110.6, 113.9, 117.5, 121.1, 124.8, 128.4, 132.4, 136.2, 140.6, 144.7, 149.0};
        TsData a = new TsData(TsFrequency.Quarterly, 2007, 3, data1, true);

        double[] weight1 = {455.0, 443.0, 431.0, 419.0, 408.0, 380.0, 352.0, 324.0, 290.0, 313.0, 336.0, 359.0,
                            395.6, 387.0, 378.4, 369.8, 368.5, 350.0, 331.5, 313.0, 290.9, 311.8, 306.8, 301.8};
        TsData wA = new TsData(TsFrequency.Quarterly, 2007, 3, weight1, true);

        int refYear = 2010;

        double result = KIXECalc.weightInRefYear(a, wA, refYear);

        double expResult = 313.77435;

        org.junit.Assert.assertEquals(expResult, result, 0.005);

    }

    @Test
    public void TestAddToFactor() {
        double[] data1 = {75.1, 78.1, 79.9, 79.7, 81.3, 86.1, 89.2, 90.8, 92.4, 95.6, 98.8, 103.5, 102.0,
                          107.4, 110.6, 113.9, 117.5, 121.1, 124.8, 128.4, 132.4, 136.2, 140.6, 144.7, 149.0};
        TsData a = new TsData(TsFrequency.Quarterly, 2007, 3, data1, true);

        double[] data2 = {114.0, 113.6, 113.8, 111.8, 110.2, 108.5, 106.8, 105.1, 103.4, 102.5, 101.7, 98.5, 97.3,
                          97.6, 96.8, 95.8, 94.6, 93.9, 92.9, 91.9, 90.8, 90.2, 89.3, 88.3, 88.1};
        TsData b = new TsData(TsFrequency.Quarterly, 2007, 3, data2, true);

        double weightA_InRefYear = 313.77435;
        double weightB_InRefYear = 353.96518;

        int refYear = 2010;

        double result = KIXECalc.addToFactor(0, 0, a, weightA_InRefYear, refYear);
        result = KIXECalc.addToFactor(result, weightA_InRefYear, b, weightB_InRefYear, refYear);

        double expResult = 99.98825;

        org.junit.Assert.assertEquals(expResult, result, 0.005);
    }

    @Test
    public void TestSubtractFromFactor() {
        double[] data1 = {75.1, 78.1, 79.9, 79.7, 81.3, 86.1, 89.2, 90.8, 92.4, 95.6, 98.8, 103.5, 102.0,
                          107.4, 110.6, 113.9, 117.5, 121.1, 124.8, 128.4, 132.4, 136.2, 140.6, 144.7, 149.0};
        TsData a = new TsData(TsFrequency.Quarterly, 2007, 3, data1, true);

        double[] data2 = {114.0, 113.6, 113.8, 111.8, 110.2, 108.5, 106.8, 105.1, 103.4, 102.5, 101.7, 98.5, 97.3,
                          97.6, 96.8, 95.8, 94.6, 93.9, 92.9, 91.9, 90.8, 90.2, 89.3, 88.3, 88.1};
        TsData b = new TsData(TsFrequency.Quarterly, 2007, 3, data2, true);

        double weightA_InRefYear = 313.77435;
        double weightB_InRefYear = 353.96518;

        int refYear = 2010;

        double result = KIXECalc.addToFactor(0, 0, a, weightA_InRefYear, refYear);
        result = KIXECalc.subtractFromFactor(result, weightA_InRefYear, b, weightB_InRefYear, refYear);

        double expResult = 100.19518;

        org.junit.Assert.assertEquals(expResult, result, 0.005);
    }

    @Test
    public void TestScaleToRefYear() {
        double factor = 99.98825;
        double[] chainedData = {102.78260, 104.55982, 103.87845, 105.02323, 108.69476, 110.88040, 111.75495, 112.62951,
                                113.80690, 115.04507, 115.63297, 114.09541, 117.17132, 118.39658, 119.55319, 120.74828,
                                121.68159, 122.42515, 123.12783, 123.91713, 124.45306, 124.88021, 125.12620, 126.18389};
        TsData chainedTs = new TsData(TsFrequency.Quarterly, 2008, 0, chainedData, true);
        int refYear = 2010;

        TsData result = KIXECalc.scaleToRefYear(chainedTs, factor, refYear);

        double[] expResultData = {89.64233, 91.19234, 90.59808, 91.59650, 94.79864, 96.70486, 97.46761, 98.23036, 99.25723,
                                  100.33710, 100.84984, 99.50885, 102.19151, 103.26013, 104.26887, 105.31118, 106.12517,
                                  106.77367, 107.38652, 108.07491, 108.54232, 108.91486, 109.12940, 110.05187};
        assertEquals(expResultData, result.internalStorage());
    }

    @Test
    public void TestAllTogetherAddAToB() {
        double[] data1 = {75.1, 78.1, 79.9, 79.7, 81.3, 86.1, 89.2, 90.8, 92.4, 95.6, 98.8, 103.5, 102.0,
                          107.4, 110.6, 113.9, 117.5, 121.1, 124.8, 128.4, 132.4, 136.2, 140.6, 144.7, 149.0};
        TsData a = new TsData(TsFrequency.Quarterly, 2007, 3, data1, true);

        double[] weight1 = {455.0, 443.0, 431.0, 419.0, 408.0, 380.0, 352.0, 324.0, 290.0, 313.0, 336.0, 359.0,
                            395.6, 387.0, 378.4, 369.8, 368.5, 350.0, 331.5, 313.0, 290.9, 311.8, 306.8, 301.8};
        TsData wA = new TsData(TsFrequency.Quarterly, 2007, 3, weight1, true);

        double[] data2 = {114.0, 113.6, 113.8, 111.8, 110.2, 108.5, 106.8, 105.1, 103.4, 102.5, 101.7, 98.5, 97.3,
                          97.6, 96.8, 95.8, 94.6, 93.9, 92.9, 91.9, 90.8, 90.2, 89.3, 88.3, 88.1};
        TsData b = new TsData(TsFrequency.Quarterly, 2007, 3, data2, true);

        double[] weight2 = {176.0, 181.0, 186.0, 191.0, 195.0, 230.0, 265.0, 300.0, 366.0, 380.0, 394.0, 408.0,
                            430.5, 463.0, 495.5, 528.0, 558.0, 576.0, 594.0, 612.0, 648.6, 672.3, 698.0, 723.7};
        TsData wB = new TsData(TsFrequency.Quarterly, 2007, 3, weight2, true);

        int refYear = 2010;

        TsData chainedAggregate = KIXECalc.chain(KIXECalc.addToWeightSum(KIXECalc.unchain(a), wA, KIXECalc.unchain(b), wB));

        double factor = KIXECalc.addToFactor(KIXECalc.addToFactor(0, 0, a, KIXECalc.weightInRefYear(a, wA, refYear), refYear),
                                             KIXECalc.weightInRefYear(a, wA, refYear), b, KIXECalc.weightInRefYear(b, wB, refYear), refYear);

        TsData result = KIXECalc.scaleToRefYear(chainedAggregate, factor, refYear);

        double[] expResultData = {87.2155, 89.64233, 91.19234, 90.59808, 91.59650, 94.79864, 96.70486, 97.46761, 98.23036, 99.25723,
                                  100.33710, 100.84984, 99.50885, 102.19151, 103.26013, 104.26887, 105.31118, 106.12517,
                                  106.77367, 107.38652, 108.07491, 108.54232, 108.91486, 109.12940, 110.05187};
        assertEquals(expResultData, result.internalStorage());
    }

    @Test
    public void TestAllTogetherAddAToB2() {
        double[] data1 = {89.78, 93.71, 90.85, 95.20, 95.47, 98.29, 100.15, 104.76, 107.69, 109.16, 112.40, 122.26,
                          119.73, 120.83, 122.45, 128.06, 128.39, 127.53, 125.12, 118.38, 102.32, 100.80, 105.21, 109.90};
        TsData a = new TsData(TsFrequency.Quarterly, 2004, 0, data1, true);

        double[] weight1 = {175.09, 184.14, 178.95, 187.45, 188.09, 194.33, 197.97, 207.50, 214.56, 219.10, 225.54, 245.62,
                            241.17, 244.79, 247.22, 257.58, 259.71, 260.26, 256.27, 240.33, 204.06, 199.94, 208.43, 218.29};
        TsData wA = new TsData(TsFrequency.Quarterly, 2004, 0, weight1, true);

        double[] data2 = {81.19, 93.96, 95.71, 97.31, 89.17, 94.84, 104.22, 111.00, 99.15, 115.17, 113.44, 132.40, 112.77,
                          118.62, 121.56, 131.39, 121.06, 121.56, 125.29, 139.41, 128.07, 129.15, 125.46, 135.75};
        TsData b = new TsData(TsFrequency.Quarterly, 2006, 1, data2, true);

        double[] weight2 = {25.15, 29.51, 30.30, 30.85, 28.51, 30.68, 32.95, 36.13, 31.86, 37.94, 37.00, 42.43, 36.29,
                            38.60, 40.18, 43.60, 40.62, 40.57, 42.46, 46.63, 40.28, 39.44, 39.73, 44.00};
        TsData wB = new TsData(TsFrequency.Quarterly, 2006, 1, weight2, true);

        int refYear = 2007;

        TsData chainedAggregate = KIXECalc.chain(KIXECalc.addToWeightSum(KIXECalc.unchain(a), wA, KIXECalc.unchain(b), wB));

        double factor = KIXECalc.addToFactor(KIXECalc.addToFactor(0, 0, a, KIXECalc.weightInRefYear(a, wA, refYear), refYear),
                                             KIXECalc.weightInRefYear(a, wA, refYear), b, KIXECalc.weightInRefYear(b, wB, refYear), refYear);

        TsData result = KIXECalc.scaleToRefYear(chainedAggregate, factor, refYear);

        double[] expResultData = {105.9522, 110.5334, 119.3295, 117.35039, 117.19164, 119.37548, 125.53398, 126.74698, 124.38076, 124.47468,
                                  118.38063, 107.10292, 103.05270, 107.68883, 112.16246};
        assertEquals(expResultData, result.internalStorage());
    }

    @Test
    public void TestWBGE_ContributionToGrowth_QuarterlyLagOne() {
        double[] unchainedA = {103.99467, 106.39148, 106.12517, 108.25566, 105.90406, 109.71710, 111.68512, 113.65314, 103.46320,
                               106.92641, 112.01299, 110.38961, 105.29412, 108.43137, 111.66667, 115.19608, 103.06383, 106.21277,
                               109.27660, 112.68085, 102.87009, 106.19335, 109.29003, 112.53776};
        TsData uA = new TsData(TsFrequency.Quarterly, 2008, 0, unchainedA, true);

        double[] weightA = {455.0, 455.0, 455.0, 455.0, 408.0, 408.0, 408.0, 408.0, 290.0, 290.0, 290.0, 290.0, 395.6, 395.6,
                            395.6, 395.6, 368.5, 368.5, 368.5, 368.5, 290.9, 290.9, 290.9, 290.9};
        TsData wA = new TsData(TsFrequency.Quarterly, 2007, 3, weightA, true);

        double[] unchainedTotal = {102.78260, 104.55982, 103.87845, 105.02323, 103.49592, 105.57703, 106.40975, 107.24248, 101.04537,
                                   102.14469, 102.66667, 101.30152, 102.69590, 103.76980, 104.78351, 105.83097, 100.77294, 101.38873,
                                   101.97067, 102.62435, 100.43249, 100.77719, 100.97570, 101.82925};
        TsData uTotal = new TsData(TsFrequency.Quarterly, 2008, 0, unchainedTotal, true);

        double[] weightTotal = {631.0, 631.0, 631.0, 631.0, 603.0, 603.0, 603.0, 603.0, 656.0, 656.0, 656.0, 656.0, 826.1,
                                826.1, 826.1, 826.1, 926.5, 926.5, 926.5, 926.5, 939.5, 939.5, 939.5, 939.5};
        TsData wTotal = new TsData(TsFrequency.Quarterly, 2007, 3, weightTotal, true);

        int lag = 1;

        TsData result = KIXECalc.contributionToGrowth(uA, wA, uTotal, wTotal, lag);

        double[] expResultData = {1.681493, -0.183657, 1.478893, 3.994786, 2.492819, 1.261255, 1.251385, 1.530989, 1.515150,
                                  2.201427, -0.699011, 2.535229, 1.462919, 1.493023, 1.612995, 1.218587, 1.242831, 1.201896,
                                  1.327819, 0.888674, 1.024560, 0.951438, 0.995888};

        assertEquals(expResultData, result.internalStorage());
    }

    @Test
    public void TestWBGE_QuarterlyLagOne() {
        double[] unchainedA = {103.99467, 106.39148, 106.12517, 108.25566, 105.90406, 109.71710, 111.68512, 113.65314, 103.46320,
                               106.92641, 112.01299, 110.38961, 105.29412, 108.43137, 111.66667, 115.19608, 103.06383, 106.21277,
                               109.27660, 112.68085, 102.87009, 106.19335, 109.29003, 112.53776};
        TsData uA = new TsData(TsFrequency.Quarterly, 2008, 0, unchainedA, true);

        double[] weightA = {455.0, 456.0, 457.0, 458.0, 408.0, 409.0, 410.0, 411.0, 290.0, 291.0, 292.0, 293.0, 395.6, 394.6,
                            393.6, 392.6, 368.5, 367.5, 366.5, 365.5, 290.9, 291.9, 292.9, 293.9};
        TsData wA = new TsData(TsFrequency.Quarterly, 2007, 3, weightA, true);

        double[] unchainedTotal = {102.78260, 104.55982, 103.87845, 105.02323, 103.49592, 105.57703, 106.40975, 107.24248, 101.04537,
                                   102.14469, 102.66667, 101.30152, 102.69590, 103.76980, 104.78351, 105.83097, 100.77294, 101.38873,
                                   101.97067, 102.62435, 100.43249, 100.77719, 100.97570, 101.82925};
        TsData uTotal = new TsData(TsFrequency.Quarterly, 2008, 0, unchainedTotal, true);

        double[] weightTotal = {631.0, 632.0, 633.0, 634.0, 603.0, 601.0, 606.0, 604.0, 656.0, 676.0, 626.0, 636.0, 826.1,
                                829.1, 816.1, 823.1, 926.5, 920.5, 927.5, 921.5, 939.5, 931.5, 933.5, 949.5};
        TsData wTotal = new TsData(TsFrequency.Quarterly, 2007, 3, weightTotal, true);

        int lag = 1;

        TsData result = KIXECalc.contributionToGrowth(uA, wA, uTotal, wTotal, lag);

        double[] expResultData = {1.681493, -0.183657, 1.478893, 3.994786, 2.492819, 1.261255, 1.251385, 1.530989, 1.515150,
                                  2.201427, -0.699011, 2.535229, 1.462919, 1.493023, 1.612995, 1.218587, 1.242831, 1.201896,
                                  1.327819, 0.888674, 1.024560, 0.951438, 0.995888};

        assertEquals(expResultData, result.internalStorage());
    }

    @Test
    public void TestWBGE_ContributionToGrowth_QuarterlyLagFour() {
        double[] unchainedA = {103.99467, 106.39148, 106.12517, 108.25566, 105.90406, 109.71710, 111.68512, 113.65314, 103.46320,
                               106.92641, 112.01299, 110.38961, 105.29412, 108.43137, 111.66667, 115.19608, 103.06383, 106.21277,
                               109.27660, 112.68085, 102.87009, 106.19335, 109.29003, 112.53776};
        TsData uA = new TsData(TsFrequency.Quarterly, 2008, 0, unchainedA, true);

        double[] weightA = {455.0, 455.0, 455.0, 455.0, 408.0, 408.0, 408.0, 408.0, 290.0, 290.0, 290.0, 290.0, 395.6, 395.6,
                            395.6, 395.6, 368.5, 368.5, 368.5, 368.5, 290.9, 290.9, 290.9, 290.9};
        TsData wA = new TsData(TsFrequency.Quarterly, 2007, 3, weightA, true);

        double[] unchainedTotal = {102.78260, 104.55982, 103.87845, 105.02323, 103.49592, 105.57703, 106.40975, 107.24248, 101.04537,
                                   102.14469, 102.66667, 101.30152, 102.69590, 103.76980, 104.78351, 105.83097, 100.77294, 101.38873,
                                   101.97067, 102.62435, 100.43249, 100.77719, 100.97570, 101.82925};
        TsData uTotal = new TsData(TsFrequency.Quarterly, 2008, 0, unchainedTotal, true);

        double[] weightTotal = {631.0, 631.0, 631.0, 631.0, 603.0, 603.0, 603.0, 603.0, 656.0, 656.0, 656.0, 656.0, 826.1,
                                826.1, 826.1, 826.1, 926.5, 926.5, 926.5, 926.5, 939.5, 939.5, 939.5, 939.5};
        TsData wTotal = new TsData(TsFrequency.Quarterly, 2007, 3, weightTotal, true);

        int lag = 4;

        TsData result = KIXECalc.contributionToGrowth(uA, wA, uTotal, wTotal, lag);

        double[] expResultData = {7.071192, 7.889490, 9.472371, 9.237943, 6.652463, 5.632791, 6.603563, 4.592968, 5.571957, 5.503102,
                                  4.813595, 7.277047, 5.873127, 5.641882, 5.339490, 5.043598, 4.700673, 4.478372, 4.222756, 3.882103};

        assertEquals(expResultData, result.internalStorage());
    }

    private void assertEquals(double[] expected, double[] actual) {
        assertArrayEquals(expected, actual, DELTA);
    }
}
