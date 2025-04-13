@echo off
cd /d "./Djangobackend"
call activate AIBOX
call python manage.py runserver