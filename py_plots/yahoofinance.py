# -*- coding: utf-8 -*-
import pandas as pd
import numpy as np
import urllib.request
from datetime import date

def download_historical_data(symbol, from_date=date(2000, 3, 1), to_date=date(2014, 3, 1)):
    baseurl = 'http://ichart.finance.yahoo.com/table.csv?ignore=.csv' \
              '&s={0}&a={1}&b={2}&c={3}&d={4}&e={5}&f={6}&g=d'
    url = baseurl.format(symbol, 
                         from_date.day-1, from_date.month-1, from_date.year,
                         to_date.day-1, to_date.month-1, to_date.year)
    
    response = urllib.request.urlopen(url)
    data = response.read()
    
    with open('tmp.txt', 'wb') as file:
        file.write(data)
        
    return pd.DataFrame.from_csv('tmp.txt')
    
def calc_daily_rate_of_return(finance_data):
    finance_data['RateOfReturn'] = np.convolve(np.log(finance_data['Close']), 
                                               np.array([0, 1, -1]), 'same')
        
def group_into_weeks(dataframe):
    def yearweek(d):
        return d.year * 100 + d.weekofyear

    weeks = dataframe.groupby(yearweek)    
    
    # Calculate the weekly closing prices
    weekly_closing_prices = {}
    for yweek, week_data in weeks:
        # Copy the Closing price of the last day in week
        weekly_closing_prices[yweek] = week_data['Close'].iloc[0]
    
    d = {'ClosingPrice': weekly_closing_prices}
    weekly_data = pd.DataFrame(d)
    
    # Calculate the weekly rate of return
    weekly_data['RateOfReturn'] = np.convolve(np.log(weekly_data['ClosingPrice']), np.array([-1, 1, 0]), 'same')
    
    # Calculate volatility
    volatility = {}
    for yweek, week_data in weeks:
        vol = 0
        for data in week_data.RateOfReturn:
            vol += abs(data - weekly_data.RateOfReturn[yweek])
            
        volatility[yweek] = vol
    
    weekly_data['Volatility'] = pd.Series(volatility)
    
    # Change index to week numbers
    c, x  = weekly_data.shape
    weekly_data = weekly_data.set_index(np.arange(0, c))
    
    return weekly_data[:-1]
    
    
    