#language: fr
@order-01
Fonctionnalité: Construction de la base utilisateurs

  L'application assure la synchronisation en temps réel des informations relatives aux utilisateurs et à leurs comptes.
  Cette synchronisation s'appuie sur la consommation d'événements métiers diffusés via Kafka, permettant ainsi de maintenir une base de données de consultation performante et à jour.

  Chaque événement reçu déclenche un processus de mise à jour qui garantit la cohérence globale du système, en traitant les créations et modifications d'entités de manière asynchrone.

  Contexte: La base de données est vide
    Étant donné que la base de données est vide

  Scénario: Une base de données vide ne contient aucune donnée métier
    Alors il devrait y avoir 0 utilisateur dans la base de données
    Et il devrait y avoir 0 compte dans la base de données

  Scénario: La réception d'un événement utilisateur crée un nouvel utilisateur dans le système

  Dès qu'un message contenant les informations d'un utilisateur est publié sur le topic Kafka dédié, l'application crée l'entrée correspondante en base de données.

    Quand un événement utilisateur est envoyé pour l'id "user-1" avec le nom "John" et l'email "john@example.com"
    Alors l'utilisateur "user-1" devrait finir par exister avec le nom "John" et l'email "john@example.com"

  Scénario: L'association entre un utilisateur et son compte est établie lors de la réception des événements respectifs

  Le système gère les relations entre les entités. Un compte ne peut être créé que si l'utilisateur auquel il est rattaché existe déjà dans le système.

    Quand un événement utilisateur est envoyé pour l'id "user-2" avec le nom "Jane" et l'email "jane@example.com"
    Et qu'un événement de compte est envoyé pour l'id "account-1" pour l'utilisateur "user-2"
    Alors l'utilisateur "user-2" devrait finir par exister avec le nom "Jane" et l'email "jane@example.com"
    Et que le compte "account-1" pour l'utilisateur "user-2" devrait finir par exister

  Scénario: Le système traite efficacement des volumes importants d'événements par lots

  La capacité de traitement de l'application permet de gérer des flux de données conséquents, assurant l'intégration rapide de multiples utilisateurs et comptes simultanément.

    Quand les événements utilisateur suivants sont envoyés :
      | id     | name  | email             |
      | user-3 | Bob   | bob@example.com   |
      | user-4 | Alice | alice@example.com |
    Et que les événements de compte suivants sont envoyés :
      | id        | userId |
      | account-2 | user-3 |
      | account-3 | user-4 |
    Alors l'utilisateur "user-3" devrait finir par exister avec le nom "Bob" et l'email "bob@example.com"
    Et l'utilisateur "user-4" devrait finir par exister avec le nom "Alice" et l'email "alice@example.com"
    Et que le compte "account-2" pour l'utilisateur "user-3" devrait finir par exister
    Et que le compte "account-3" pour l'utilisateur "user-4" devrait finir par exister

  Plan du scénario: La robustesse du traitement est validée sur diverses combinaisons de données

  Différents profils d'utilisateurs et de comptes sont testés pour s'assurer que le système réagit correctement quel que soit le contenu des messages Kafka.

    Quand un événement utilisateur est envoyé pour l'id "<userId>" avec le nom "<name>" et l'email "<email>"
    Et qu'un événement de compte est envoyé pour l'id "<accountId>" pour l'utilisateur "<userId>"
    Alors l'utilisateur "<userId>" devrait finir par exister avec le nom "<name>" et l'email "<email>"
    Et que le compte "<accountId>" pour l'utilisateur "<userId>" devrait finir par exister

    Exemples:
      | userId | name  | email             | accountId |
      | user-x | Alice | alice@example.com | account-x |
      | user-y | Bob   | bob@example.com   | account-y |

  Scénario: Les événements orphelins ne sont pas intégrés pour préserver l'intégrité des données

  Si un événement de compte fait référence à un utilisateur inconnu, le compte n'est pas créé afin d'éviter toute incohérence dans la base de données.

    Quand un événement de compte est envoyé pour l'id "account-fail" pour l'utilisateur "unknown-user"
    Alors il devrait y avoir 0 compte dans la base de données
