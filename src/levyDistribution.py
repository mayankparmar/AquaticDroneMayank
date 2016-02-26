# -*- coding: utf-8 -*-
"""
Created on Wed Feb 10 14:49:01 2016

@author: mayank
"""

import math
import numpy as np
import matplotlib.pyplot as plt
import scipy.interpolate as intplt


"""
Functions
"""

###### Assigning series of numbers to variable
def rangeCreator(start, stop, step):
    r = []
    while start < stop:
        r.append(round(start,2))
        start += step
        #print("%f" % r)
    return r


###### Levy Distribution

def levyDist(x, mu = 0, sigma = 0.5):

    levy = []
    for num in range (0, len(x)):
        if x[num] > mu:
            g1 = math.exp(-sigma / (2*(x[num] - mu)))
            g2 = math.pow((sigma / (x[num] - mu)), (3/2));
            levy.append((g1 * g2) / math.sqrt(2 * math.pi * (math.pow(sigma,2))))
        else:
            levy.append(0)
        
    return levy
   
   
##### Normal Distribution

def normalDist(x, mu = 0, sigma = 0.5):
    
    normal = []
    
    for num in range(0, len(x)):
        temp = math.exp(-((math.pow((x[num] - mu), 2))/(2 * math.pow(sigma, 2))) )
        temp2 = 1/(sigma * math.sqrt(2 * math.pi))
        normal.append(temp*temp2)
        
    return normal

        
##### Cumulative Distribution Function Levy
    
def levyCDF(x, mu = 0, sigma = 0.5):
    
    F = []
    for num in range(0, len(x)):
        temp = math.sqrt(sigma / (2*(x[num] - mu)))
        F.append(math.erfc(temp))
        
    return F
    

##### Cumulative Distribution Function Normal

def normalCDF(x, mu = 0, sigma = 0.5):
    


      F = []
      
      for num in range(0, len(x)):
          temp = math.erf((x[num] - mu) / (sigma * math.sqrt(2)))
          F.append(0.5 * (1 + temp))
      
      return F



#==============================================================================

 
#     numBins = len(x)
#     counts, binEdges = np.histogram(x, bins = numBins, normed = True)
#     CDF = np.cumsum(counts)
#     
#     
#     return CDF, binEdges
#     
#==============================================================================
"""
XXX
"""

"""
main
"""
def main():
    
    xLevy = rangeCreator(0.1, 100, 0.1)
    muLevy, sigmaLevy = 0, 0.27674
    levy = levyDist(xLevy, muLevy, sigmaLevy)
    CDFLevy = levyCDF(xLevy, muLevy, sigmaLevy)

    plt.figure(0)
    
    plt.plot(xLevy, levy, 'r-', lw = 2)

    plt.plot(xLevy, CDFLevy, 'b-')
    

    plt.figure(1)
    xNormal = rangeCreator(-5, 5, 0.1)
    muNormal, sigmaNormal = 0, 1
    normal = normalDist(xNormal, muNormal, sigmaNormal)
#    CDFNormal, binEdges = normalCDF(x, mu, sigma)
    
    CDFNormal = normalCDF(xNormal, muNormal, sigmaNormal)
    
    plt.plot(xNormal, normal, 'r-', lw = 2)
    plt.plot(xNormal, CDFNormal, 'b-')
#    plt.plot(binEdges[1:], CDFNormal, 'b-')
    
    plt.figure(2)

#    bx = plt.subplot(212)
#    levySamples = random.sample(levy, 100)
#    brownianSamples = random.sample(normal, 100)
#    bx.plot(range(0, 100), levySamples, 'r-')
#    bx.plot(range(0, 100), brownianSamples, 'g-')
    
    ### Random number generation
    invCDFLevy = intplt.interp1d(CDFLevy, xLevy, bounds_error = False, fill_value = 0)
    invCDFNormal = intplt.interp1d(CDFNormal, xNormal)
    
    samples = np.random.rand(10)
    randLevy = invCDFLevy(samples)
    randNormal = invCDFNormal(samples)
    
    x = rangeCreator(0, 10, 1)
    
    plt.plot(x, randLevy, 'rx')
    plt.plot(x, randNormal, 'bo')
    print("Levy random:", randLevy)
    print("Normal Random: ", randNormal)

if __name__ == "__main__":
    main()


#class levyPDF(st.rv_continuous):
#    def _pdf(self, x):
#        if x > mu:
#            g1 = math.exp(-sigma / (2*(x - mu)))
#            g2 = math.pow((sigma / (x - mu)), (3/2))
#            levy = (g1 * g2) / math.sqrt(2 * math.pi * (math.pow(sigma,2)))
#        else:
#            levy = 0
#        return levy
#
#levyRV = levyPDF(a = 0.0, b = 0, name = 'levyPDF')