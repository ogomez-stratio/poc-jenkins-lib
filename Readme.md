<img src="http://ic.sanitas.dom/git/data-science/jenkins-pipeline-lib/raw/develop/resources/logosanitas.gif" align = "left"/>
<img src="http://ic.sanitas.dom/git/data-science/jenkins-pipeline-lib/raw/develop/resources/logo-stratio-blue.png" align= "right"/>


# Sanitas Connected Data
# Librerias Jenkins
## Tabla de contenidos

- [Introducción](#Introducción)
- [Configuración](#configuracion)
- [API](#api)
- [Getting Started](#getting-started)
    - [Entorno Local](#entorno-local)
        - [Prerequisitos](#prerequisitos)
        - [Instalación ydespliegue](#instalacion-y-despliegue)
- [Casos de uso](#casosdeuso)
- [Changelog](#changelog)


## Introducción

El objetivo del proyecto es proveer de librerías de uso general para modelizar pipelines de despliegue 
en jenkins usando el API de Jenkins Pipeline.


## Getting Started
### Entorno Local

Siguiendo estas instrucciones podrás tener un copia del proyecto funcionando en tu entorno local (docker compose).

#### Prerequisitos

* Configurar la librería como libreria global en Jenkins:

  

* Debes ejecutar e intalar (maven clean install) el proyectos de scd-commons que podras encontrar [aquí](http://ic.sanitas.dom/git/data-science/scd-commons.git)

* La configuración necesaría para este entorno se encuentra en los properties application.yml y application-dev.yml

* Tener instalado docker y docker compose. Mas info [aquí](https://docs.docker.com/engine/installation/)

#### Instalación y despliegue

* Descarga y descomprime el proyecto, o bien clonalo: [scd-nps](http://ic.sanitas.dom/git/data-science/scd-nps.git)
* Ve a la carpeta `/environment`
* Ejecuta  los scripts de construcción y arranque

```
./build-images.sh && ./handle-containers.sh start

```
Estos scripts arrancarán todos los recursos necesarios para la ejecución de la aplicación, también efecturarán la carga de la tabla maestra de preguntas smile.

* Comprueba el estado de los servicios.

```
curl scddatanps:8091/admin/health


```

## Casos de Uso

<img src="http://ic.sanitas.dom/git/data-science/scd-nps/raw/develop/resources/arquitectura.png" align="center"/>
