import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { HTTP_INTERCEPTORS, provideHttpClient } from '@angular/common/http'; // Import provideHttpClient

import { routes } from './app.routes';
import { AuthService } from './security/auth.service';
import { AuthInterceptor } from './security/auth.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(), // Use provideHttpClient instead of HttpClientModule
    // Add AuthService to providers if you want to inject it into components or other services
    AuthService,

    // Register the AuthInterceptor globally
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true,
    },
  ],
};
