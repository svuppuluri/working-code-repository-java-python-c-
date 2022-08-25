# -*- coding: utf-8 -*-
"""
Created on Tue Aug 23 19:33:12 2022

@author: Gerardo Fernández-Escribano
"""

#from numpy import mean, median
#import numpy

#age is a variable
age  = 25

#ages is an array (list in Python)
ages = [24, 25, 22, 20, 21, 23, 30, 22, 24, 23, 27]

#printing values
print(age)
print(ages[0])
print(ages[10])
print(ages)

#printing values with text
print("age number "+ str(0) +" is: " + str(ages[0]))

#calculating the mean of the values in the ages array
ages_mean = (24 + 25 + 22 + 20 + 21 + 23 + 30 + 22 + 24 + 23 +27) / 11

#printing the mean value
print ("age mean is = " + str(ages_mean))

#write senteces to print on the console the highest and the lower values
#of the ages array



#now we are going to use the numpy library to do the same
#ages_mean_numpy = numpy.mean(ages)
#ages_mean_numpy = mean(ages) 
#print ("age mean with NumPy is = " + str(ages_mean_numpy))

"""
brand = ["Tesla", "Acura", "Ford", "BMW", "Mercedes", "Volvo", 
         "Volkswagen", "Ford", "Tesla", "Volkswagen", "Acura", 
         "Mercedes", "Mercedes", "Volvo", "BMW"]
speed = [120, 100, 110, 105, 135, 95, 89, 97, 100, 107, 128, 85, 122, 120, 97]
cars = []
cars.append(brand)
cars.append(speed)

#printing values from the brand list
print(cars[0])
print(cars[1][1])
print(cars)

"""

"""
brand = ["Tesla", "Acura", "Ford", "BMW", "Mercedes", "Volvo", 
         "Volkswagen", "Ford", "Tesla", "Volkswagen", "Acura", 
         "Mercedes", "Mercedes", "Volvo", "BMW"]
color = ["red", "black", "grey","white","black","black","red", "black",
         "grey","white","grey","white","blue","black","red"]
speed = [120, 100, 110, 105, 135, 95, 89, 97, 100, 107, 128, 85, 122, 120, 97]
age_old = [5, 5, 7, 10, 1, 12, 2, 9, 14, 11, 10, 7, 5, 4, 1]
sunpass = ["Yes", "Yes", "No", "No", "No", "No", "Yes", "Yes", "No", 
           "No", "Yes", "Yes", "No", "Yes", "Yes"]

cars_extended = []
cars_extended.append(brand)
cars_extended.append(color)
cars_extended.append(age_old)
cars_extended.append(speed)
cars_extended.append(sunpass)

#printing values from the brand list
print(cars_extended)

#printing the mean value
car_extended_mean = mean(cars_extended[3])
print ("car_extended spead mean is = " + str(car_extended_mean))
#print ("car_extended age_old mode is = " + str(stats.mode(speed)))
print ("car_extended spead median is = " + str(median(cars_extended[3])))


"""

"""
#we are going to open a file with the car information
cars_file = open("cars_updated.txt")
cars_file_content = cars_file.read()
print(cars_file_content)

#calculate the mean, the mode and the median of some of the information just
#we read from the file
print(mean(cars_file_content[2]))

#we close the file....just in case
cars_file.close()
"""