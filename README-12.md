# Menssageria

### **necessidades**

Adicionar o um serviço no kafka que vai simular um outro microserviço que vai realizar a ação de enviar um cupom de desconto para o cliente. Para facilitar a consulta da implementação. O serviço vai ser implementado nesse proprio repositório.

### **resultado esperado**

- Ao finalizar um compra deve ser enviado para o kafka um evento de compra finalizada.
- O consumidor vai simular a criação de desconto, como a ideia é entender o fluxo de menssageria, não vamos implementar a lógica de criação do cupom.

### Referencias

[docker](https://docs.docker.com/guides/kafka/)

[implementação  ](https://guides.micronaut.io/latest/micronaut-kafka-maven-java.html)
