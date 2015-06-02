# Script for downloading and transforming yahoo finance data for predicting
# future stock prices with the OutputOnlyLearning program
# the data is from today 2 years back

import sys
import csv
import datetime

import pandas as pd
from pandas.io.data import DataReader

def generate_training_set(stock_data, prediction_days, training_days):
    """
    Generates a training data set for further processing
    """
    with open('training_data.csv', 'w', newline='') as csvfile:
        writer = csv.writer(csvfile, delimiter=',')

        writer.writerow([prediction_days])
        writer.writerow([training_days])

        for i in range(0, training_days):
            sub = list(stock_data.iloc[i:i+prediction_days+1])
            writer.writerow([1.0,] + sub)

def generate_test_set(stock_data, from_day, test_days):
    """
    Generates a list of data points to test against.
    """
    with open('test_data.csv', 'w', newline='') as csvfile:
        writer = csv.writer(csvfile, delimiter=',')
        writer.writerow(stock_data.iloc[from_day:from_day+test_days])

if __name__ == '__main__':
    if len(sys.argv) < 2:
        symbol = 'MSFT'
    else:
        symbol = sys.argv[1]

    # Download historic stock prices from yahoo
    today = datetime.datetime.now()
    ts = DataReader(symbol, 'yahoo', 
            today-datetime.timedelta(days=2 * 365),
            today)

    # Normalize data to 0.0-1.0
    ts['normalized'] = (ts.Close-ts.Close.min())/(ts.Close.max()-ts.Close.min())

    prediction_days = 30
    training_days = 400
    test_days = 100
    generate_training_set(ts.normalized, prediction_days, training_days)
    generate_test_set(ts.normalized, training_days, test_days)

