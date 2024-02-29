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

    cy.url().should('include', '/sessions')
  })

  it('Login failed', () => {
    cy.visit('/login')

    cy.get('input[formControlName=email]').type("allan@toto.com")
    cy.get('input[formControlName=password]').type(`${"allan.1234"}{enter}{enter}`)

    cy.url().should('not.include', '/sessions')
  })

  it('Logout', () => {
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
})