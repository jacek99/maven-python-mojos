from freshen import *
from freshen.checks import *
from freshen.parser import Table
from nose import *

@Given('I have the following students in my database:')
def enter(steps):
    pass
    
@When('I bill names starting with "G"')
def i_bill_names_starting_with_g():
    pass
    
@Then('I see those billed students:')
def i_see_those_billed_students(steps):
    pass
    
@Then("those that weren't:")
def and_those_who_werent(steps):
    pass

