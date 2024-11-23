import { Component } from '@angular/core';
import { InvestmentService } from '../investment.service';
import { InvestmentResultRow } from '../../model/result.model';

@Component({
  selector: 'app-result',
  standalone: true,
  imports: [],
  templateUrl: './result.component.html',
  styleUrls: ['./result.component.css'],
})
export class ResultComponent {
  constructor(private investmentService: InvestmentService) {}

  get investmentsByYear(): InvestmentResultRow[] {
    return this.investmentService.getInvestmentResults();
  }
}
