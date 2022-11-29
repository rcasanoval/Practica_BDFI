# Practica_BDFI. Practica Predicción de vuelos.

**Práctica realizada por Ricardo Alfonso Casanova Lozano y Pablo Ruiz Giles**

A continuación se detallan los comandos a seguir para realizar el despliegue de la práctica con docker-compose.

## Clonar el repositorio.

Para empezar se debe clonar el repositorio con el siguiente comando: 

```
git clone https://github.com/rcasanoval/Practica_BDFI/
```
Una vez con el repositorio clonado se debe de acceder a la carpeta con el comando: 

```
cd Practica_BDFI/
```

## Construir los servicios.

Se debe de confirmar que en la máquina virtual se tiene el servicio docker-compose con el comando: 
```
docker-compose --version
```
NOTA: Si no está instalado se recomienda seguir la instalación en el siguiente enlace: https://www.digitalocean.com/community/tutorials/how-to-install-and-use-docker-compose-on-ubuntu-20-04

Para la realización de la práctica se han construido 3 ficheros Dockerfiles, lo que nos permite personalizar las imágenes a utilizar.
- **Dockerfile**: Imagen creada para spark, con esta imagen se descargan las librerías y los datos. 
- **Dockerfile.scala**: Imagen creada para scala-sbt, con esta imagen se descargan las librerías y los datos. 
- **Dockerfile.python**: Imagen creada para la web, con esta imagen se descargan las librerías y se expone el puerto 5000.

Los servicios personalizados se construyen ejecutando el siguiente comando: 

```
docker-compose build
```

## Levantar los servicios.

A continuación se procederá a levantar los servicios con el comando: 

```
docker-compose up  -d
```
Una vez se ejecute el comando se debe proceder a ejecutar los siguientes comandos para que la práctica funcione correctamente: 

## Crear el topic en Kafka.

Para crear el topic en Kafka se debe de ejecutar el siguiente comando: 

```
docker-compose exec kafka bin/kafka-topics.sh --create --bootstrap-server kafka:9092 --replication-factor 1 --partitions 1 --topic flight_delay_classification_request
```
(Opcional). Si se quiere confirmar que el topic ha sido creado correctamente se puede ejecutar el siguiente comando: 
```
docker-compose exec kafka bin/kafka-topics.sh --bootstrap-server kafka:9092 --list
```
## Importar las distancias recorridas en Mongo.

A continuación, se procederá a importar las distancias recorridas en MongoDB con el comando: 
```
docker-compose exec mongo ./resources/import_distances.sh
```
## Crear el paquete JAR con SBT.

Para poder hacer uso del comando spark-submit es necesario crear el paquete JAR con SBT, para ello se debe de ejecutar el siguiente comando: 
```
docker-compose exec scala sbt package
```
## Entrenar al modelo.
Para entrenar el modelo se debe de ejecutar el siguiente comando: 
```
docker-compose exec spark python3 resources/train_spark_mllib_model.py .
```
## Ejecutar el predictor de vuelos.
Una vez el modelo entrenado y guardado, se procederá a poner en marcha el predictor de vuelos. Esto se realiza ejecutando el comando: 
```
docker-compose exec spark bash -c "cd flight_prediction && spark-submit --packages org.mongodb.spark:mongo-spark-connector_2.12:3.0.1,org.apache.spark:spark-sql-kafka-0-10_2.12:3.1.2 --class es.upm.dit.ging.predictor.MakePrediction target/scala-2.12/flight_prediction_2.12-0.1.jar" &
```
Al ejecutar este comando nos debemos de dirigir a la página web donde está corriendo la aplicación: http://<IP_MV>:5000/flights/delays/predict_kafka
- <IP_MV>: Este valor se debe de sustituir por la dirección IP de la máquina virtual. En nuestro caso quedaría de la siguiente forma: http://20.229.194.248:5000/flights/delays/predict_kafka

En la página web se deben de realizar las pruebas para confirmar que recibimos la predicción con los datos introducidos.
