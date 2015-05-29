# coding: utf-8
import sys
import yahoofinance 
import numpy as np
import matplotlib.pyplot as plt

if __name__ == '__main__':
    if len(sys.argv) < 2:
        symbol = 'MSFT'
    else:
        symbol = sys.argv[1]
    
    d = yahoofinance.download_historical_data(symbol)
    
    mask200 = np.append(np.ones((200,)), np.zeros((199,))) / 200
    mask50 = np.append(np.ones((50,)), np.zeros((49,)))/50
    
    d['200_dayma'] = np.convolve(d.Close, mask200, mode='same')
    d['50_dayma'] = np.convolve(d.Close, mask50, mode='same')
    
    
    d = d.iloc[:-1000]
    
    # Calculate crossovers
    (rows, columns) = d.shape
    d['50_is_higher'] = d['50_dayma'] > d['200_dayma']
    d['Crossover'] = np.zeros((rows,))
    
    for i in range(1, rows):
        if d.iloc[i-1]['50_is_higher'] == False and d.iloc[i]['50_is_higher'] == True:
            d.Crossover.iloc[i] = 1
        
        if d.iloc[i-1]['50_is_higher'] == True and d.iloc[i]['50_is_higher'] == False:
            d.Crossover.iloc[i] = -1
            
    crossover = d[d.Crossover == 1]
    crossunder = d[d.Crossover == -1]
    
    plt.figure()
    plt.plot(d.index, d.Close, label='{}: Closing Prices'.format(symbol))
    plt.plot(d.index, d['200_dayma'], label='200 day ma')
    plt.plot(d.index, d['50_dayma'], label='50 day ma')
    
    plt.scatter(crossover.index, crossover['Close'], s=40, color='red',
            label='Sell')
    plt.scatter(crossunder.index, crossunder['Close'], s=40, color='green',
            label='Buy')
    
    plt.legend()
    
    plt.show()
