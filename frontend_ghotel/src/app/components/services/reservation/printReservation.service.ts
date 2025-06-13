// src/app/services/ticket.service.ts
import { Injectable, OnInit } from '@angular/core';
import { Reservation } from '../../../components/models/reservation';
import jsPDF from 'jspdf';
import { AuthService } from '../auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class PrintReservationService   {
   
    utilisateur!: string
 
  
  constructor(private authService : AuthService) {}
 
  
 async generateTicket(factureReservation: Reservation): Promise<void> {
  this.utilisateur = this.authService.getUserName();
  const logoUrl = 'assets/logo-ticket-gurutmu.png';
  const logoData = await this.getImageData(logoUrl);

  const doc = new jsPDF({
    orientation: 'portrait',
    unit: 'mm',
    format: [58, 200],
  });

  let y = 5;

  // Ajout du logo
  if (logoData) {
    const logoWidth = 50;
    const logoHeight = 15;
    const logoX = (58 - logoWidth) / 2;
    doc.addImage(logoData, 'PNG', logoX, y, logoWidth, logoHeight, undefined, 'FAST');
    y += logoHeight + 3;
  }

  // Infos établissement (en haut)
  doc.setFont('helvetica', 'normal');
  doc.setFontSize(6);
  
  y += 3;
  doc.text('Gurutumu Palace, Les saveurs du Wassulu.', 29, y, { align: 'center' });
  y += 3;
  doc.text('Immeuble Gurutumu, Diombougou Yanfolila, Mali.', 29, y, { align: 'center' });
  y += 3;
  doc.text('Tel: +223 78 19 19 5 / +223 68 19 19 58', 29, y, { align: 'center' });
  y += 3;
  doc.text('Email: gurutumu80@gmail.com', 29, y, { align: 'center' });
  y += 5;

  // Infos générales
  doc.setFontSize(9);
  doc.text(`Gérant : ${this.utilisateur}`, 5, y);
  y += 4;
  doc.text(`N° Ticket: ${factureReservation.id}`, 5, y);
  y += 4;
  doc.text(`Client: ${factureReservation.client.prenom} ${factureReservation.client.nom}`, 5, y);
  y += 4;
  doc.text(`Date: ${new Date(factureReservation.dateReservation).toLocaleDateString()}`, 5, y);
  y += 2;
  doc.line(5, y, 53, y);
  y += 4;

  // Entête tableau
  doc.setFontSize(8);
  doc.setFont('helvetica', 'bold');
  doc.text('Article', 5, y);
  doc.text('Qte', 30, y);
  doc.text('P.U', 41, y, { align: 'right' });
  doc.text('Total', 53, y, { align: 'right' });
  y += 3;
  doc.line(5, y, 53, y);
  y += 3;

  // Lignes
  doc.setFont('helvetica', 'normal');
  factureReservation.lignes?.forEach(ligne => {
    const nomObjet = ligne.chambre?.numero || ligne.salle?.numero || 'Objet inconnu';
    const nomAffiche = nomObjet.length > 15 ? nomObjet.substring(0, 12) + '...' : nomObjet;

    const qte = ligne.nombreJours.toString();
    const pu = ligne.prixUnitaire.toFixed(0);
    const total = (ligne.nombreJours * ligne.prixUnitaire).toFixed(0);

    doc.text(nomAffiche, 5, y);
    doc.text(qte, 30, y);
    doc.text(pu, 41, y, { align: 'right' });
    doc.text(total, 53, y, { align: 'right' });
    y += 5;
  });

  y += 2;
  doc.line(5, y, 53, y);
  y += 4;

  // Montant total
  doc.setFontSize(10);
  doc.setFont('helvetica', 'bold');
  doc.text(`Montant Total: ${factureReservation.montantTotal?.toFixed(0)} FCFA`, 53, y, { align: 'right' });
  doc.text(`Montant Remise: ${factureReservation.facture?.montantRemise?.toFixed(0)} FCFA`, 53, y+4, { align: 'right' });
  doc.text(`Montant Net: ${factureReservation.facture?.montantNet?.toFixed(0)} FCFA`, 53, y+8, { align: 'right' });
  
  doc.setFontSize(6);
  doc.text('Merci pour votre achat !', 29, y+12, { align: 'center' });
  // Impression
  const pdfUrl = doc.output('bloburl');
  const win = window.open(pdfUrl, '_blank');
  win?.print();
}


  async generateFacture(factureReservation: Reservation): Promise<void> {
  this.utilisateur = this.authService.getUserName();
  console.log('TICKET DE VENTE ', factureReservation);
  const logoUrl = 'assets/logo-ticket-gurutmu.png';
  const logoData = await this.getImageData(logoUrl);

  const doc = new jsPDF({
    orientation: 'portrait',
    unit: 'mm',
    format: 'a4' // ✅ Format A4
  });

  let currentY = 10;

  // Largeur disponible sur A4 avec marges
  const pageWidth = 210;
  const margin = 15;
  const usableWidth = pageWidth - margin * 2;

  if (logoData) {
    const logoWidth = 60;
    const logoHeight = 20;
    const logoX = (pageWidth - logoWidth) / 2;
    const logoY = currentY;

    doc.addImage(logoData, 'PNG', logoX, logoY, logoWidth, logoHeight, undefined, 'FAST');

    currentY = logoY + logoHeight + 10;

    doc.setFontSize(14);
    doc.text(`Facture N°: ${factureReservation.facture?.numero}`, margin, currentY);
    currentY += 8;
    doc.text(`Reservation N°: ${factureReservation.code}`, margin, currentY);
    currentY += 10;

    doc.setFontSize(11);
    doc.text(`Client: ${factureReservation.client.prenom} ${factureReservation.client.nom}`, margin, currentY);
    currentY += 6;
    doc.text(`Date réservation: ${new Date(factureReservation.dateReservation).toLocaleDateString()}`, margin, currentY);
    currentY += 8;
    doc.text(`Date Facture: ${factureReservation.facture?.dateFacture}`, margin, currentY);
    currentY += 10;
    doc.text(`Gérant: ${this.utilisateur}`, margin, currentY);
    currentY += 12;
    doc.line(margin, currentY, pageWidth - margin, currentY);
    currentY += 6;
  }

  // Titres colonnes
  doc.setFontSize(11);
  doc.setFont('helvetica', 'bold');
  doc.text('Designation', margin, currentY);
  doc.text('Qte', margin + 90, currentY);
  doc.text('P.U', margin + 120, currentY, { align: 'left' });
  doc.text('Total', pageWidth - margin, currentY, { align: 'right' });

  currentY += 5;
  doc.line(margin, currentY, pageWidth - margin, currentY);
  currentY += 4;

  // Lignes réservation
  doc.setFont('helvetica', 'normal');
  factureReservation.lignes?.forEach(ligne => {
    const nomObjet = ligne.chambre?.numero || ligne.salle?.numero || 'Objet inconnu';
    const nomAffiche = nomObjet.length > 30 ? nomObjet.substring(0, 27) + '...' : nomObjet;

    const prixUnitaire = ligne.prixUnitaire.toFixed(0);
    const total = (ligne.nombreJours * ligne.prixUnitaire).toFixed(0);

    doc.text(nomAffiche, margin, currentY);
    doc.text(ligne.nombreJours.toString(), margin + 90, currentY);
    doc.text(prixUnitaire, margin + 120, currentY, { align: 'left' });
    doc.text(total, pageWidth - margin, currentY, { align: 'right' });

    currentY += 6;
  });

  currentY += 4;
  doc.line(margin, currentY, pageWidth - margin, currentY);
  currentY += 8;

  // Montants
  doc.setFont('helvetica', 'bold');
  doc.setFontSize(12);
  doc.text(`Montant Total : ${factureReservation.facture?.montantTotal?.toFixed(0)} FCFA`, pageWidth - margin, currentY, { align: 'right' });
  currentY += 6;
  doc.text(`Remise : ${factureReservation.facture?.montantRemise?.toFixed(0)} FCFA`, pageWidth - margin, currentY, { align: 'right' });
  currentY += 6;
  doc.text(`Net à payer : ${factureReservation.facture?.montantNet?.toFixed(0)} FCFA`, pageWidth - margin, currentY, { align: 'right' });
  currentY += 6;
  doc.text(`Montant Payé : ${factureReservation.facture?.montantPaye?.toFixed(0)} FCFA`, pageWidth - margin, currentY, { align: 'right' });
  currentY += 6;
  doc.text(`Reste à Payer : ${factureReservation.facture?.resteAPayer?.toFixed(0)} FCFA`, pageWidth - margin, currentY, { align: 'right' });

  currentY += 10;

  // Footer
  doc.setFont('helvetica', 'normal');
  doc.setFontSize(10);
  doc.text('Merci pour votre confiance !', pageWidth / 2, currentY, { align: 'center' });
  currentY += 6;
  doc.text('Gurutumu Palace - Les saveurs du Wassulu', pageWidth / 2, currentY, { align: 'center' });
  currentY += 5;
  doc.text('Immeuble Gurutumu, Diombougou Yanfolila - Mali', pageWidth / 2, currentY, { align: 'center' });
  currentY += 5;
  doc.text('+223 78 19 19 58 / +223 68 19 19 58 - gurutumu80@gmail.com', pageWidth / 2, currentY, { align: 'center' });

  // Générer et afficher
  const pdfUrl = doc.output('bloburl');
  window.open(pdfUrl, '_blank');
}

 

  
  private async getImageData(url: string): Promise<string | null> {
    try {
      const response = await fetch(url);
      const blob = await response.blob();
      return new Promise((resolve) => {
        const reader = new FileReader();
        reader.onloadend = () => resolve(reader.result as string);
        reader.readAsDataURL(blob);
      });
    } catch (error) {
      console.error('Erreur de chargement du logo', error);
      return null;
    }
  }
}
