# HowTo
Constant Streaming:
<pre>curl -H "Accept:application/x-ndjson" http://localhost:8080/quotes</pre>

Just 20 and stop
<pre>curl -H "Accept:application/json" http://localhost:8080/quotes</pre>

# Starting local instance of mongo and mongo-express
<pre> sudo docker run --network some-network --name mongo -p27017:27017 -d mongo:latest </pre>
<pre> sudo docker run --network some-network -p 8081:8081 mongo-express </pre>

# Start rabbitmq
<pre> sudo docker run -d --hostname my-rabbit -p 5672:5672 --name some-rabbit rabbitmq:3 </pre>
