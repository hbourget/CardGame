import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CardsComponent} from "./cards/cards.component";
import {InventoryComponent} from "./inventory/inventory.component";
import {MarketComponent} from "./market/market.component";
import {RankingComponent} from "./ranking/ranking.component";
import {HomeComponent} from "./home/home.component";
import {RoomsComponent} from "./rooms/rooms.component";
import {GameComponent} from "./game/game.component";
import {RegisterComponent} from "./register/register.component";
import {LoginComponent} from "./login/login.component";
import { AuthGuard } from './auth.guard';
import {LogoutComponent} from "./logout/logout.component"; // Import the AuthGuard

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent},
  { path: 'cards', component: CardsComponent, canActivate: [AuthGuard] },
  { path: 'inventory', component: InventoryComponent, canActivate: [AuthGuard] },
  { path: 'ranking', component: RankingComponent, canActivate: [AuthGuard] },
  { path: 'market', component: MarketComponent, canActivate: [AuthGuard] },
  { path: 'rooms', component: RoomsComponent, canActivate: [AuthGuard] },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'logout', component: LogoutComponent },
  { path: 'game/:idRoom', component: GameComponent, canActivate: [AuthGuard] }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
