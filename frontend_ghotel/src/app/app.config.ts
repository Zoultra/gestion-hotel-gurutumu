import { ApplicationConfig, inject, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { HttpErrorResponse, provideHttpClient, withInterceptors  } from '@angular/common/http';
import { Ng2SearchPipeModule } from 'ng2-search-filter';
 
import { jwtErrorInterceptor } from '../app/components/services/auth/jwt-error.interceptor';
import { AuthService } from '../app/components/services/auth/auth.service';  
import { AlertService } from '../app/components/services/alert/alert.service'; 
import { tap } from 'rxjs';

export const appConfig: ApplicationConfig = {
  providers: [provideZoneChangeDetection({ eventCoalescing: true }), 
              provideRouter(routes),  
              provideHttpClient(),Ng2SearchPipeModule,
              
              provideHttpClient(
                withInterceptors([
                  (req, next) => {
                     
                   
                     const token = localStorage.getItem('auth_token');
          
                    if (token) {
                       const isFormData = req.body instanceof FormData;
                      req = req.clone({
                        setHeaders: {
                          Authorization: `Bearer ${token}`,
                           ...(isFormData ? {} : { 'Content-Type': 'application/json' })
                        },
                      });
                    }
                    const alertService = inject(AlertService);
                     const authService = inject(AuthService);
                       // 🚨 Vérifie si le token est expiré
                    
                    
          
                    return next(req).pipe(
    tap({
      error: (error: HttpErrorResponse) => {
        if (error.status === 401) {
          alertService.confirmError('Session expirée, veuillez vous reconnecter', 4000);
          authService.logout();
        }
      }
    })
  );
                  }
                ]),
                withInterceptors([jwtErrorInterceptor])
              )
            ]
};
