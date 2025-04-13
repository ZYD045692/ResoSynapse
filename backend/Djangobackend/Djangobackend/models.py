from django.db import models

class Service(models.Model):
    service_id = models.AutoField(primary_key=True)
    user_id = models.IntegerField()
    service_name = models.CharField(max_length=255)
    created_time = models.DateTimeField(auto_now_add=True)
    last_time = models.DateTimeField(auto_now=True)
 
    class Meta:
        db_table = 'services'  

class File(models.Model):
    id = models.AutoField(primary_key=True)
    service = models.ForeignKey(Service, on_delete=models.CASCADE, related_name='file')
    file_url = models.CharField(max_length=255)
    position_index = models.IntegerField()
    class Meta:
        db_table = 'file'