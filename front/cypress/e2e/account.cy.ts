/// <reference types="cypress" />
describe('Login spec', () => {
  it('Login successfull', () => {
    cy.visit('/login')

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
    cy.wait('@session')

    cy.url().should('include', '/sessions')
  })

  it('Login failed', () => {
    cy.visit('/login')

    cy.get('input[formControlName=email]').type("allan@toto.com")
    cy.get('input[formControlName=password]').type(`${"allan.1234"}{enter}{enter}`)

    cy.url().should('not.include', '/sessions')
  })

  it('Logout', () => {
    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')
        

    cy.visit('/login')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.get('.mat-toolbar > .ng-star-inserted > :nth-child(3)').click()

    cy.url().should('not.include', '/sessions')
  })

  it('Login required fields' , () => {
    cy.visit('/login')

    cy.get('input[formControlName=email]').type("yoga@studio.com")

    cy.get('.mat-raised-button').should('be.disabled')
  })

  it('display info user', () => {
    const currentDate = new Date().toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.visit('/login')
    
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
    
    cy.get('[routerlink="me"]').click()

    cy.get('.mat-card-content > div.ng-star-inserted > :nth-child(1)').should('contain', 'Admin ADMIN')
    cy.get('.mat-card-content > div.ng-star-inserted > :nth-child(2)').should('contain', 'yoga@studio.com')

    cy.get('.mat-card-content > div.ng-star-inserted > :nth-child(3)').should('contain', 'admin')

    cy.get('.p2 :nth-child(1)').should('contain', currentDate)
    cy.get('.p2 :nth-child(2)').should('contain', currentDate)

  })
})