# Plantilla

A template for application development.

Contains:

* A DropWizard application
* Docker dev setup for supporting applications
* Metrics reporting to graphite
* Gradle building of project
* Ansible setup for deployment

See presentation for details. You build it with:

```
landslide config.cfg --relative --copy-theme
```

# Future directions

Elements to add:

* Password protection and access control by annotation
* Explicit metrics upon app usage
