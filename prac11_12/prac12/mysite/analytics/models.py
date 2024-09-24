from django.db import models

class Book(models.Model):
    author = models.CharField(max_length=100)
    seller_id = models.IntegerField()
    product_type = models.CharField(max_length=50)
    cost = models.DecimalField(max_digits=10, decimal_places=2)
    title = models.CharField(max_length=100)

class Telephone(models.Model):
    manufacturer = models.CharField(max_length=100)
    battery_capacity = models.IntegerField()
    seller_id = models.IntegerField()
    product_type = models.CharField(max_length=50)
    cost = models.DecimalField(max_digits=10, decimal_places=2)
    title = models.CharField(max_length=100)

class WashingMachine(models.Model):
    manufacturer = models.CharField(max_length=100)
    drum_capacity = models.IntegerField()
    seller_id = models.IntegerField()
    product_type = models.CharField(max_length=50)
    cost = models.DecimalField(max_digits=10, decimal_places=2)
    title = models.CharField(max_length=100)
