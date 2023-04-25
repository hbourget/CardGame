import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CardsComponent} from "./cards/cards.component";
import {InventoryComponent} from "./inventory/inventory.component";
import {MarketComponent} from "./market/market.component";
import {RankingComponent} from "./ranking/ranking.component";
import {HomeComponent} from "./home/home.component";


const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'cards', component: CardsComponent },
  { path: 'inventory', component: InventoryComponent },
  { path: 'ranking', component: RankingComponent },
  { path: 'market', component: MarketComponent },
  { path: 'login', component: CardsComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
