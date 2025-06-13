import { Role } from "./role";
 
export class Utilisateur{

    id!: number;

    prenom!: string;

    nom!: string;

    email!: string;

    username!: string;

    telephone!: string;

    password!: string;

    
    role!: Role;

   
}