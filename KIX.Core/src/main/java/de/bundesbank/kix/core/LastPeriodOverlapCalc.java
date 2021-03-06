/*
 * Copyright 2017 Deutsche Bundesbank
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

import ec.tstoolkit.timeseries.simplets.*;

/**
 *
 * @author Thomas Witthohn
 */
public abstract class LastPeriodOverlapCalc implements ICalc {

    public static TsData contributionToGrowth(TsData unchainedContributor, TsData weightsContributor, TsData unchainedTotal, TsData weightsTotal, int lag) {
        if (lag < 0) {
            lag = unchainedContributor.getFrequency().intValue() * lag * -1;
        }
        TsDomain domain = unchainedContributor.lead(lag).getDomain().intersection(unchainedContributor.getDomain());
        TsData returnValue = new TsData(domain, 0);
        TsFrequency frequency = returnValue.getFrequency();

        for (TsObservation tsObservation : returnValue) {
            double value;
            TsPeriod period = tsObservation.getPeriod();
            int year = period.getYear();
            TsPeriod lastPeriodOneYearAgo = new TsPeriod(frequency, year - 1, frequency.intValue() - 1);
            TsPeriod lastPeriodTwoYearsAgo = new TsPeriod(frequency, year - 2, frequency.intValue() - 1);

            if (period.getPosition() < lag) {
                TsPeriod laggedPeriod = new TsPeriod(frequency, year - 1, period.getPosition() + frequency.intValue() - lag);
                value = ((weightsContributor.get(lastPeriodOneYearAgo) / weightsTotal.get(lastPeriodOneYearAgo))
                        * (unchainedTotal.get(lastPeriodOneYearAgo) / unchainedTotal.get(laggedPeriod))
                        * (unchainedContributor.get(period) - 100))
                        + ((weightsContributor.get(lastPeriodTwoYearsAgo) / weightsTotal.get(lastPeriodTwoYearsAgo))
                        * (100 / unchainedTotal.get(laggedPeriod))
                        * (unchainedContributor.get(lastPeriodOneYearAgo) - unchainedContributor.get(laggedPeriod)));
            } else {
                TsPeriod laggedPeriod = new TsPeriod(frequency, year, period.getPosition() - lag);
                value = (weightsContributor.get(lastPeriodOneYearAgo) / weightsTotal.get(lastPeriodOneYearAgo))
                        * (100 / unchainedTotal.get(laggedPeriod))
                        * (unchainedContributor.get(period) - unchainedContributor.get(laggedPeriod));
            }
            returnValue.set(period, value);
        }

        return returnValue;
    }

    /**
     *
     * @param chainedTs
     * @param factor
     * @param refYear
     *
     * @return
     */
    public static TsData scaleToRefYear(TsData chainedTs, double factor, int refYear) {
        return chainedTs.times(factor).div(meanInRefYear(chainedTs, refYear));
    }

    /**
     *
     * @param factor
     * @param weightFactor
     * @param index
     * @param weightIndex_InRefYear
     * @param refYear
     *
     * @return
     */
    public static double subtractFromFactor(double factor, double weightFactor, TsData index, double weightIndex_InRefYear, int refYear) {
        return (factor * weightFactor - meanInRefYear(index, refYear) * weightIndex_InRefYear) / (weightFactor - weightIndex_InRefYear);
    }

    /**
     *
     * @param factor
     * @param weightFactor
     * @param index
     * @param weightIndex_InRefYear
     * @param refYear
     *
     * @return
     */
    public static double addToFactor(double factor, double weightFactor, TsData index, double weightIndex_InRefYear, int refYear) {
        return (factor * weightFactor + meanInRefYear(index, refYear) * weightIndex_InRefYear) / (weightFactor + weightIndex_InRefYear);
    }

    /**
     *
     * @param weightSum
     *
     * @return
     */
    public static TsData chain(TsData weightSum) {
        TsData retVal = weightSum.clone();
        double chainingFactor = 100;
        int startYear = retVal.getStart().getYear();
        int endYear = retVal.getLastPeriod().getYear();
        TsFrequency frequency = retVal.getFrequency();

        for (int i = 1; i <= endYear - startYear; ++i) {
            chainingFactor *= weightSum.get(new TsPeriod(frequency, startYear + i - 1, frequency.intValue() - 1)) / 100;
            if (i == endYear - startYear) {
                for (int k = 0; k <= retVal.getLastPeriod().getPosition(); ++k) {
                    double chainedValue = retVal.get(new TsPeriod(frequency, startYear + i, k)) * chainingFactor / 100;
                    retVal.set(new TsPeriod(frequency, startYear + i, k), chainedValue);
                }
            } else {
                for (int k = 0; k < frequency.intValue(); ++k) {
                    double chainedValue = retVal.get(new TsPeriod(frequency, startYear + i, k)) * chainingFactor / 100;
                    retVal.set(new TsPeriod(frequency, startYear + i, k), chainedValue);
                }
            }
        }
        return retVal;
    }

    /**
     * Each value in the time series is divided by the respective previous year
     * final value (month or quarter) then multiplied by 100.
     *
     * @param index
     *
     * @return new TsData
     */
    public static TsData unchain(TsData index) {
        return index.times(100).div(transform(index));

    }

    /**
     *
     * @param index
     * @param weight
     * @param refYear
     *
     * @return
     */
    public static double weightInRefYear(TsData index, TsData weight, int refYear) {
        TsFrequency frequency = index.getFrequency();
        double meanInRefYear = meanInRefYear(index, refYear);
        double lastValuePreviousYearIndex = index.get(new TsPeriod(frequency, refYear - 1, frequency.intValue() - 1));
        double lastValuePreviousYearWeight = weight.get(new TsPeriod(frequency, refYear - 1, frequency.intValue() - 1));
        return meanInRefYear / lastValuePreviousYearIndex * lastValuePreviousYearWeight;
    }

    /**
     * Returns a weighted sum defined with the formula (index1 * weight1 +
     * index2 * weight2 ) / (weight1 + weight2). The new domain is the
     * intersection of the input domains.
     *
     * @param index1
     * @param weight1
     * @param index2
     * @param weight2
     *
     * @return new TsData
     */
    public static TsData addToWeightSum(TsData index1, TsData weight1, TsData index2, TsData weight2) {
        weight1 = transform(weight1);
        weight2 = transform(weight2);

        return (index1.times(weight1).plus(index2.times(weight2))).div(weight1.plus(weight2));

    }

    /**
     * Returns a weighted sum defined with the formula (index1 * weight1 -
     * index2 * weight2 ) / (weight1 - weight2). The new domain is the
     * intersection of the input domains.
     *
     * @param index1
     * @param weight1
     * @param index2
     * @param weight2
     *
     * @return new TsData
     */
    public static TsData subtractFromWeightSum(TsData index1, TsData weight1, TsData index2, TsData weight2) {
        weight1 = transform(weight1);
        weight2 = transform(weight2);

        return (index1.times(weight1).minus(index2.times(weight2))).div(weight1.minus(weight2));

    }

    //TODO: ordentlicher Name
    /**
     *
     * @param index
     *
     * @return
     */
    private static TsData transform(TsData index) {
        int startYear = index.getStart().getYear();
        int startPosition = index.getStart().getPosition();
        int endYear = index.getLastPeriod().getYear();
        TsFrequency frequency = index.getFrequency();

        int count = frequency.intValue() * (index.getEnd().getYear() - startYear) + (frequency.intValue() - startPosition);
        TsData retVal = new TsData(new TsDomain(frequency, startYear, startPosition, count));

        //First year
        {
            double lastValueThisYear = index.get(new TsPeriod(frequency, startYear, frequency.intValue() - 1));
            for (int i = startPosition; i < frequency.intValue(); ++i) {
                retVal.set(new TsPeriod(frequency, startYear, i), lastValueThisYear);
            }
        }

        //all other years
        for (int i = 1; i <= endYear - startYear; ++i) {
            double lastValuePreviousYear = index.get(new TsPeriod(frequency, startYear + i - 1, frequency.intValue() - 1));

            for (int k = 0; k < frequency.intValue(); ++k) {
                retVal.set(new TsPeriod(frequency, startYear + i, k), lastValuePreviousYear);

            }
        }
        return retVal;
    }

    /**
     *
     * @param data
     * @param refYear
     *
     * @return
     */
    protected static double meanInRefYear(TsData data, int refYear) {

        double sum = 0;
        TsFrequency frequency = data.getFrequency();
        for (int i = 0; i < frequency.intValue(); i++) {
            sum += data.get(new TsPeriod(frequency, refYear, i));
        }
        return sum / frequency.intValue();
    }

}
