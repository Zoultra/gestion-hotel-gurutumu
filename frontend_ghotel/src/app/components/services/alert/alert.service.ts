
import { Injectable } from '@angular/core';
import Swal from 'sweetalert2';

@Injectable({
  providedIn: 'root', // Disponible dans toute l'application
})
export class AlertService {
  constructor() {}

  // Afficher une alerte de chargement
  showLoading(message: string, duration?: number) {
    Swal.fire({
      title: message,
      didOpen: () => {
        Swal.showLoading(); // Affiche l'indicateur de chargement
      },
      timer: duration, // Ferme automatiquement après la durée spécifiée
      timerProgressBar: true, // Affiche une barre de progression du timer
      allowOutsideClick: false, // Empêche la fermeture en cliquant à l'extérieur
      allowEscapeKey: false, // Empêche la fermeture avec la touche Échap
    });

    
  }

  // Fermer l'alerte de chargement
  closeLoading() {
    Swal.close();
  }

  // Afficher une alerte de succès
  confirmSuccess(message: string, timer: number) {
    Swal.fire({
      icon: 'success',
      title: message,
      text: 'Vos données ont été traitées avec succès.',
      showConfirmButton: false,
      timer: timer,
    });
  }

  // Dans alert.service.ts

// Afficher une alerte d'erreur
confirmError(message: string, timer: number) {
  Swal.fire({
    icon: 'error',
    title: 'Erreur',
    text: message,
    showConfirmButton: false,
    timer: timer,
  });
}

// Afficher une alerte d'avertissement
confirmWarning(message: string = 'Attention !', timer: number = 5000) {
  Swal.fire({
    icon: 'warning',
    title: message,
    showConfirmButton: false,
    timer: timer,
  });
}


success(message: string, title: string = "Succès") {
  Swal.fire({
    title: title,
    text: message,
    icon: "success",
    confirmButtonColor: "#3085d6",
    confirmButtonText: "OK"
  });
}

error(message: string, title: string = "Erreur") {
  Swal.fire({
    title: title,
    text: message,
    icon: "error",
    confirmButtonColor: "#d33",
    confirmButtonText: "OK"
  });
}

confirmDelete(callback: () => void) {
  Swal.fire({
    title: "Êtes-vous sûr ?",
    text: "Cette action est irréversible !",
    icon: "warning",
    showCancelButton: true,
    confirmButtonColor: "#d33",
    cancelButtonColor: "#3085d6",
    confirmButtonText: "Oui, supprimer !",
    cancelButtonText: "Annuler"
  }).then((result) => {
    if (result.isConfirmed) {
      callback(); // Exécute l'action de suppression
    }
  });
}

confirmLogout(callback: () => void) {
  Swal.fire({
    title: "Confirmation",
    text: "Êtes-vous sûr de vouloir vous déconnecter ?",
    icon: "warning",
    showCancelButton: true,
    confirmButtonColor: "#d33",
    cancelButtonColor: "#3085d6",
    confirmButtonText: "Oui, déconnexion !",
    cancelButtonText: "Annuler"
  }).then((result) => {
    if (result.isConfirmed) {
      callback(); // Exécute l'action de deconnexion
    }
  });
}


confirmPayementVente(callback: () => void) {
  Swal.fire({
    title: "Confirmation",
    text: "Êtes-vous sûr de vouloir payer ?",
    icon: "warning",
    showCancelButton: true,
    confirmButtonColor: "#28a745",
    cancelButtonColor: "#d33",
    confirmButtonText: "Oui, payer !",
    cancelButtonText: "Annuler"
  }).then((result) => {
    if (result.isConfirmed) {
      callback(); // Exécute l'action de deconnexion
    }
  });
}

confirmFermetureCaisse(message: string, message2: string, callback: () => void) {
  Swal.fire({
    title: "Confirmation du montant",
    text: message,
    icon: "warning",
    showCancelButton: true,
    titleText: message2,
    confirmButtonColor: "#28a745",
    cancelButtonColor: "#d33",
    confirmButtonText: "Oui, confirmer !",
    cancelButtonText: "Annuler"
  }).then((result) => {
    if (result.isConfirmed) {
      callback(); // Exécute l'action de deconnexion
    }
  });
}


}