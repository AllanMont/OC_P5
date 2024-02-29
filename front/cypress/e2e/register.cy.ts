describe('Register spec', () => {
    it('register successfull', () => {
      cy.visit('/register')
  
      cy.intercept('POST', '/api/auth/register', {
        body: {
          email: 'allan.mont34@gmail.com',
          firstName: 'Allan',
          lastName: 'Mont',
          password: 'test!1234',
        },
      })
  
      cy.intercept('POST', '/api/auth/login', {
        body: {
          username: 'yoga@studio.com,',
          password: 'test!1234',
        },
      })
  
      cy.intercept(
        {
          method: 'GET',
          url: '/api/session',
        },
        []).as('session')
  
      cy.get('input[formControlName=email]').type("allan.mont34@gmail.com")
      cy.get('input[formControlName=firstName]').type("Allan")
      cy.get('input[formControlName=lastName]').type("Mont")
      cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
      
      cy.get('input[formControlName=email]').type("allan.mont34@gmail.com")
      cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
  
      cy.url().should('include', '/sessions')
    })

    it('Login required fields' , () => {
      cy.visit('/register')
  
      cy.get('input[formControlName=email]').type("yoga@studio.com")
  
      cy.get('.mat-raised-button').should('be.disabled')
    })
  })