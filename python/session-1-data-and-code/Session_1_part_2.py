# -*- coding: utf-8 -*-
"""
Created on Wed Aug 24 15:15:08 2022

@author: Gerardo Fernández-Escribano
"""

from pandas import read_csv
from matplotlib import pyplot
#from sklearn import preprocessing

dataset = read_csv("cars_updated_panda.txt")
print(dataset)

dataset.hist()
pyplot.show()

#we will change categorical entrys to numerical entrys
#matplotlib has to work with numbers
#string_to_int =  preprocessing.LabelEncoder() 
#dataset  = dataset.apply(string_to_int.fit_transform)

#then, we repeat the histogram operation
#print(dataset)
#dataset.hist()
#pyplot.show()
