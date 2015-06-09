# Script for downloading and transforming yahoo finance data for predicting
# future stock prices with the OutputOnlyLearning program
# the data is from today 2 years back

import sys
import csv
import datetime

import pandas as pd
from pandas.io.data import DataReader

def generate_training_set(name, stock_data, from_day, prediction_days, training_days):
    """
    Generates a training data set for further processing
    """
    with open(name, 'w', newline='') as csvfile:
        writer = csv.writer(csvfile, delimiter=',')

        writer.writerow([prediction_days])
        writer.writerow([training_days])

        for i in range(from_day, from_day+training_days):
            sub = list(stock_data.iloc[i:i+prediction_days])
            if sub[-1] > sub[-2]:
                trend = 1.0
            else:
                trend = -1.0
            writer.writerow([1.0,] + sub + [trend,])

if __name__ == '__main__':
    if len(sys.argv) < 2:
        symbol = 'MSFT'
    else:
        symbol = sys.argv[1]

    # Download historic stock prices from yahoo
    today = datetime.datetime.now()
    ts = DataReader(symbol, 'yahoo', 
            today-datetime.timedelta(days=5 * 365),
            today)

    # Normalize data to 0.0-1.0
    ts['normalized'] = (ts.Close-ts.Close.min())/(ts.Close.max()-ts.Close.min())

    prediction_days = 5
    training_days = 500
    test_days = 100
    generate_training_set('training_data.csv', 
            ts.normalized, 0, prediction_days, training_days)
    generate_training_set('test_data.csv',
            ts.normalized, training_days, prediction_days, test_days)



