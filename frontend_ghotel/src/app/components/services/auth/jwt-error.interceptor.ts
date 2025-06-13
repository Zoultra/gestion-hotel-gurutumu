// src/app/interceptors/jwt-error.interceptor.ts
import { Injectable, inject } from '@angular/core';

import {
  HttpInterceptorFn,
  HttpRequest,
  HttpHandlerFn,
  HttpErrorResponse,
} from '@angular/common/http';

import { AuthService } from '../../services/auth/auth.service';
import { AlertService } from '../../../components/services/alert/alert.service';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

export const jwtErrorInterceptor: HttpInterceptorFn = (req: HttpRequest<any>, next: HttpHandlerFn) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const alertService = inject(AlertService)
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      
      if (error.status === 403) {
        const isLoginRequest = req.url.includes('/login');

        if (isLoginRequest) {
          // Laisser le composant de login gérer cette erreur
          return throwError(() => error);
        } else {
          // Token expiré ou accès interdit
          alertService.confirmWarning("Votre session a expiré. Veuillez vous reconnecter.")
          authService.logout(); // ou autre logique
          router.navigate(['/login']);
        }
      }

      return throwError(() => error);
    })
  );
};
