import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CardsComponent} from "./cards/cards.component";
import {InventoryComponent} from "./inventory/inventory.component";
import {MarketComponent} from "./market/market.component";
import {RankingComponent} from "./ranking/ranking.component";
import {HomeComponent} from "./home/home.component";
import {RoomsComponent} from "./rooms/rooms.component";
import {GameComponent} from "./game/game.component";

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'cards', component: CardsComponent },
  { path: 'inventory', component: InventoryComponent },
  { path: 'ranking', component: RankingComponent },
  { path: 'market', component: MarketComponent },
  { path: 'rooms', component: RoomsComponent },
  { path: 'game', component: CardsComponent },
  { path: 'game/:idRoom', component: GameComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
