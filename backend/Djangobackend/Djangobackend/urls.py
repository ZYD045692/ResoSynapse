"""
URL configuration for Djangobackend project.

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/4.2/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""

from django.urls import path
from . import service
from . import pdf
from . import formula
from . import text
from . import email
from . import mdAIGC
urlpatterns = [
    path('api/createService/', service.createService, name='createService'),
    path('api/updateService/', service.updateService, name='updateService'),
    path('api/deleteService/', service.deleteService, name='deleteService'),
    path('api/inquiryService/', service.inquiryService, name='inquiryService'),
    path('api/detailedService/', service.detailedService, name='detailedService'),
    path('api/addPage/', pdf.addPage, name='addPage'),
    path('api/deletePage/', pdf.deletePage, name='deletePage'),
    path('api/movePage/', pdf.movePage, name='movePage'),
    path('api/downloadPdf/', pdf.downloadPdf, name='downloadPdf'),
    path('api/downloadImages/', pdf.downloadImages, name='downloadImages'),
    path('api/ocrFormula/', formula.ocrFormula, name='ocrFormula'),
    path('api/transformText/', text.transformText, name='transformText'),
    path('api/transformLanguage/', text.transformLanguage, name='transformLanguage'),
    path('api/getEmailCaptcha/', email.getEmailCaptcha, name='getEmailCaptcha'),
    path('api/checkEmailCaptcha/', email.checkEmailCaptcha, name='checkEmailCaptcha'),
    path('api/mdchat/', mdAIGC.mdchat, name='mdchat'),
]