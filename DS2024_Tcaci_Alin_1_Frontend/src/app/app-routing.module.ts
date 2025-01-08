import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from './login/login.component';
import {ClientComponent} from './client/client.component';
import {AdminComponent} from './admin/admin.component';
import {ChatComponent} from './chat/chat.component';

const routes: Routes = [
  {path: '', redirectTo : 'login', pathMatch: "full"},
  {path: 'login', component: LoginComponent},
  {path: 'client', component: ClientComponent},
  {path: 'admin', component: AdminComponent},
  {path: 'chat', component: ChatComponent},
  {path: '**', redirectTo: 'login'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
