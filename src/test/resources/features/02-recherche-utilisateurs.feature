#language: fr
@order-02
Fonctionnalité: Recherche d'utilisateurs

  L'application expose une interface de consultation REST permettant d'accéder aux données consolidées des utilisateurs et de leurs comptes rattachés.
  Cette interface constitue le point d'entrée principal pour les services tiers souhaitant obtenir une vue d'ensemble de la clientèle ou des détails sur un utilisateur spécifique.

  Les données retournées sont toujours synchronisées avec les derniers événements reçus via Kafka, garantissant ainsi une information fiable et précise.

  Contexte:
    Étant donné que la base de données est vide

  Scénario: Une base de données vide retourne une liste d'utilisateurs vide

  Si le système ne contient encore aucune donnée, l'appel à la liste globale des utilisateurs renvoie une réponse vide, confirmant l'absence d'enregistrements.

    Alors le point d'accès REST "/users" devrait retourner une liste vide

  Scénario: La liste globale permet de visualiser l'ensemble des utilisateurs et la synthèse de leurs comptes

  L'API offre la possibilité de l'interroger pour obtenir la liste complète des utilisateurs enregistrés, accompagnée du nombre de comptes associés à chacun.

    Étant donné qu'un utilisateur existe avec l'id "user-rest-1", le nom "Rest User" et l'email "rest@example.com"
    Et qu'un compte existe avec l'id "account-rest-1" pour l'utilisateur "user-rest-1"
    Quand j'interroge le point d'accès REST "/users"
    Alors la réponse devrait contenir l'utilisateur "user-rest-1" avec 1 compte

  Scénario: La consultation détaillée d'un utilisateur fournit une vue complète de son profil et de ses comptes

  En interrogeant l'API avec un identifiant utilisateur unique, on accède à l'intégralité des informations de son profil ainsi qu'au détail exhaustif de tous les comptes qui lui sont rattachés.

    Étant donné qu'un utilisateur existe avec l'id "user-single", le nom "Single User" et l'email "single@example.com"
    Et qu'un compte existe avec l'id "account-s1" pour l'utilisateur "user-single"
    Et qu'un compte existe avec l'id "account-s2" pour l'utilisateur "user-single"
    Quand j'interroge le point d'accès REST "/users/user-single"
    Alors la réponse devrait être l'utilisateur "user-single" avec 2 comptes
