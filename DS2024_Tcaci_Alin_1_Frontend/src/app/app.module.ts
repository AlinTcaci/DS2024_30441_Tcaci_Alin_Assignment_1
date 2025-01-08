import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import {PanelModule} from 'primeng/panel';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {ChipsModule} from 'primeng/chips';
import {ButtonDirective, ButtonModule} from 'primeng/button';
import {HttpClientModule, provideHttpClient, withFetch} from '@angular/common/http';
import {ClientComponent} from './client/client.component';
import { AdminComponent } from './admin/admin.component';
import {TableModule} from 'primeng/table';
import {InputTextModule} from 'primeng/inputtext';
import { ChatComponent } from './chat/chat.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ClientComponent,
    AdminComponent,
    ChatComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    PanelModule,
    ReactiveFormsModule,
    ChipsModule,
    ButtonDirective,
    HttpClientModule,
    TableModule,
    ButtonModule,
    InputTextModule,
    FormsModule,
  ],
  providers: [
    provideClientHydration(),
    provideHttpClient(withFetch())
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
