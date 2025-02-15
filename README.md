# Laboratorio 3

Pueden ver la consigna del laboratorio en https://docs.google.com/document/d/e/2PACX-1vQn5BpCPQ6jKMN-sz46261Qot82KbDZ1RUx8jNzAN4kBEAq_i97T3R6ZA0_yRA5elN66e-EArXQXuAh/pub

# Preparación del entorno
- [X] Necesitan Java 17, tanto el JRE como el JDK. En Ubuntu, pueden instalarlo con:

```bash
apt install openjdk-17-jdk openjdk-17-jre
```

- [X] Instalar [maven](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html) Es probable que sea un paquete de tu distribución (`$ sudo apt install maven` si estás en Ubuntu, Debian o derivados).
- [X] Descargar [spark 3.5.1](https://www.apache.org/dyn/closer.lua/spark/spark-3.5.1/spark-3.5.1-bin-hadoop3.tgz) y descomprimirlo en el directorio `DIR`.
- [X] Definir variable de entorno `export SPARK_HOME=<DIR>` (ahí `<DIR>` es el directorio donde descomprimieron spark).

 
# Cómo compilarlo

Sea `APP_HOME` el directorio donde está este archivo `README.md`

```bash
$ cd $APP_HOME
$ mvn package
```

# Cómo usarlo
Primero seteamos la cantidad de trabajadores que queremos en nuestro cluster.
```bash
$ export SPARK_WORKER_INSTANCES=2
```
A su vez, podemos especificar su memoria y cores a utilizar:
Se recomienda usar por lo menos 2G para cada trabajador, de lo contrario no se puede trabajar con CoreNLP (los archivos de los modelos son pesados)
```bash
$ export SPARK_WORKER_MEMORY=2G
$ export SPARK_WORKER_CORES=1
```
Corremos los trabajadores y el master
```bash
$ $SPARK_HOME/sbin/start-master.sh
$ $SPARK_HOME/sbin/start-worker.sh  spark://your-host:7077
```
Ahora la aplicación
```bash
$ $SPARK_HOME/bin/spark-submit --executor-memory 2G  --class App   --master spark://your-host:7077   target/App-0.1-jar-with-dependencies.jar  [ARGS]
```
Si no quieren ver la información de spark pueden redirigir `stderr` a `/dev/null`:
```bash
$ $SPARK_HOME/bin/spark-submit --executor-memory 2G  --class App   --master spark://your-host:7077   target/App-0.1-jar-with-dependencies.jar  [ARGS] 2>/dev/null
```

Luego de correr el programa hay que eliminar el archivo de la big data:
```bash
$ rm $APP_HOME/src/main/resources/bigData.txt
```
