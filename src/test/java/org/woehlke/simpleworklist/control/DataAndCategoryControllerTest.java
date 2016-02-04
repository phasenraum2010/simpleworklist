package org.woehlke.simpleworklist.control;

import static org.hamcrest.Matchers.*;
import static org.woehlke.simpleworklist.entities.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.woehlke.simpleworklist.AbstractTest;
import org.woehlke.simpleworklist.entities.Category;
import org.woehlke.simpleworklist.entities.ActionItem;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.services.CategoryService;
import org.woehlke.simpleworklist.services.ActionItemService;
import org.woehlke.simpleworklist.services.UserService;


public class DataAndCategoryControllerTest extends AbstractTest {

    private static final Logger logger = LoggerFactory.getLogger(DataAndCategoryControllerTest.class);

    @Inject
    private UserService userService;

    @Inject
    private ActionItemService actionItemService;

    @Inject
    private CategoryService categoryService;

    @Test
    public void testHome() throws Exception {
        makeActiveUser(emails[0]);
        this.mockMvc.perform(
                get("/")).andDo(print())
                .andExpect(view().name(containsString("redirect:/category/0/page/1")));
    }

    @Test
    public void testCategoryZeroRedirect() throws Exception {
        makeActiveUser(emails[0]);
        this.mockMvc.perform(
                get("/category/0/")).andDo(print())
                .andExpect(view().name(containsString("redirect:/category/0/page/1")));
    }

    @Test
    public void testCategoryZero() throws Exception {
        makeActiveUser(emails[0]);
        this.mockMvc.perform(
                get("/category/0/page/1")).andDo(print())
                .andExpect(view().name(containsString("category/show")))
                .andExpect(model().attributeExists("breadcrumb"))
                .andExpect(model().attributeExists("thisCategory"))
                .andExpect(model().attributeExists("dataList"))
                .andExpect(model().attributeExists("beginIndex"))
                .andExpect(model().attributeExists("endIndex"))
                .andExpect(model().attributeExists("currentIndex"));
    }

    @Test
    public void testFormNewCategoryNode() throws Exception {
        makeActiveUser(emails[0]);
        this.mockMvc.perform(
                get("/category/addchild/0")).andDo(print())
                .andExpect(model().attributeExists("breadcrumb"))
                .andExpect(model().attributeExists("thisCategory"))
                .andExpect(model().attribute("thisCategory", notNullValue()))
                .andExpect(model().attribute("thisCategory", instanceOf(Category.class)))
                .andExpect(model().attribute("thisCategory", hasProperty("id")))
                .andExpect(model().attribute("thisCategory", is(categoryNullObject())));
    }

    @Test
    public void testHelperCategoryCreateTree() throws Exception {
        makeActiveUser(emails[0]);
        this.mockMvc.perform(
                get("/test/helper/category/createTree")).andDo(print())
                .andExpect(view().name(containsString("redirect:/")));
        UserAccount user = userService.retrieveCurrentUser();
        List<Category> rootCategories = categoryService.findRootCategoriesByUserAccount(user);
        Assert.assertTrue(rootCategories.size() > 0);
        for(Category rootCategory:rootCategories){
            Assert.assertTrue(rootCategory.isRootCategory());
            for(Category child:rootCategory.getChildren()){
                Assert.assertFalse(child.isRootCategory());
                Assert.assertEquals(child.getParent().getId().longValue(),rootCategory.getId().longValue());
            }
        }
    }

    @Test
    public void testShowChildNodePage() throws Exception {
        makeActiveUser(emails[0]);
        logger.info("----------------------------------------------");
        logger.info("testShowChildNodePage");
        logger.info("----------------------------------------------");
        UserAccount user = userService.retrieveCurrentUser();
        logger.info(user.toString());
        logger.info("----------------------------------------------");
        Date nowDate = new Date();
        long now = nowDate.getTime();
        String name01 = "test01" + now;
        String name02 = "test02" + now;
        String name0101 = "test0101" + now;
        String name0102 = "test0102" + now;
        String name0201 = "test0201" + now;
        String name0202 = "test0202" + now;
        String name020201 = "test020201" + now;
        String name020202 = "test020202" + now;
        String name020203 = "test020203" + now;
        Category c01 = Category.newRootCategoryNodeFactory(user);
        c01.setName(name01);
        c01.setDescription("description01 for " + name01);
        c01 = categoryService.saveAndFlush(c01);
        Category c02 = Category.newRootCategoryNodeFactory(user);
        c02.setName(name02);
        c02.setDescription("description02 for " + name02);
        c02 = categoryService.saveAndFlush(c02);
        Category c0101 = Category.newCategoryNodeFactory(c01);
        c0101.setName(name0101);
        c0101.setDescription("description0101 for " + name0101);
        c0101 = categoryService.saveAndFlush(c0101);
        Category c0102 = Category.newCategoryNodeFactory(c01);
        c0102.setName(name0102);
        c0102.setDescription("description0102 for " + name0102);
        c0102 = categoryService.saveAndFlush(c0102);
        Category c0201 = Category.newCategoryNodeFactory(c02);
        c0201.setName(name0201);
        c0201.setDescription("description0201 for " + name0201);
        c0201 = categoryService.saveAndFlush(c0201);
        Category c0202 = Category.newCategoryNodeFactory(c02);
        c0202.setName(name0202);
        c0202.setDescription("description0202 for " + name0202);
        c0202 = categoryService.saveAndFlush(c0202);
        Category c020201 = Category.newCategoryNodeFactory(c0202);
        Category c020202 = Category.newCategoryNodeFactory(c0202);
        Category c020203 = Category.newCategoryNodeFactory(c0202);
        c020201.setName(name020201);
        c020202.setName(name020202);
        c020203.setName(name020203);
        c020201.setDescription("description for " + name020201);
        c020202.setDescription("description for " + name020202);
        c020203.setDescription("description for " + name020203);
        c020201 = categoryService.saveAndFlush(c020201);
        c020202 = categoryService.saveAndFlush(c020202);
        c020203 = categoryService.saveAndFlush(c020203);
        logger.info(c01.toString());
        logger.info(c02.toString());
        logger.info(c0101.toString());
        logger.info(c0102.toString());
        logger.info(c0201.toString());
        logger.info(c0202.toString());
        logger.info(c020201.toString());
        logger.info(c020202.toString());
        logger.info(c020203.toString());
        logger.info("----------------------------------------------");
        this.mockMvc.perform(
                get("/category/" + c01.getId() + "/page/1")).andDo(print())
                .andExpect(view().name(containsString("category/show")))
                .andExpect(model().attribute("thisCategory", c01))
                .andExpect(model().attribute("thisCategory", is(categoryNotNullObject())));
        logger.info("----------------------------------------------");
        this.mockMvc.perform(
                get("/category/" + c0202.getId() + "/page/1")).andDo(print())
                .andExpect(view().name(containsString("category/show")))
                .andExpect(model().attribute("thisCategory", c0202))
                .andExpect(model().attribute("thisCategory", is(categoryNotNullObject())));
        logger.info("----------------------------------------------");
        this.mockMvc.perform(
                get("/category/" + c020202.getId() + "/page/1")).andDo(print())
                .andExpect(view().name(containsString("category/show")))
                .andExpect(model().attribute("thisCategory", c020202))
                .andExpect(model().attribute("thisCategory", is(categoryNotNullObject())));
        logger.info("----------------------------------------------");
    }

    @Test
    public void rootCategoriesNonNullPrecondition() throws Exception {
        makeActiveUser(emails[0]);
        UserAccount user = userService.retrieveCurrentUser();
        List<Category> rootCategories = categoryService.findRootCategoriesByUserAccount(user);
        for (Category category : rootCategories) {
            this.mockMvc.perform(
                    get("/category/" + category.getId() + "/page/1")).andDo(print())
                    .andExpect(model().attributeExists("breadcrumb"))
                    .andExpect(model().attributeExists("thisCategory"))
                    .andExpect(model().attributeExists("dataList"))
                    .andExpect(view().name(containsString("category/show")))
                    .andExpect(model().attribute("thisCategory", category))
                    .andExpect(model().attribute("thisCategory", is(categoryNotNullObject())));
        }
    }

    @Test
    public void testNewDataLeafForRootCategoryNode() throws Exception {
        makeActiveUser(emails[0]);
        this.mockMvc.perform(
                get("/actionItem/addtocategory/0")).andDo(print())
                .andExpect(model().attributeExists("thisCategory"))
                .andExpect(model().attributeExists("breadcrumb"))
                .andExpect(model().attribute("thisCategory", notNullValue()))
                .andExpect(model().attribute("thisCategory", instanceOf(Category.class)))
                .andExpect(model().attribute("thisCategory", hasProperty("id")))
                .andExpect(model().attribute("thisCategory", is(categoryNullObject())))
                .andExpect(view().name(containsString("actionItem/add")));
    }

    @Test
    public void testUserList() throws Exception {
        makeActiveUser(emails[0]);
        this.mockMvc.perform(get("/users")).andDo(print())
                .andExpect(model().attributeExists("users"))
                .andExpect(view().name(containsString("user/users")));
    }

    @Test
    public void testEditDataFormCategory() throws Exception {
        makeActiveUser(emails[0]);
        UserAccount user = userService.retrieveCurrentUser();
        List<Category> rootCategories = categoryService.findRootCategoriesByUserAccount(user);
        int i = 0;
        for (Category cat:rootCategories){
            i++;
            ActionItem ai = new ActionItem();
            ai.setCategory(cat);
            ai.setTitle("Title_"+i);
            ai.setText("Text_"+i);
            actionItemService.saveAndFlush(ai);
        }
        for (Category cat:rootCategories){
            int pageNr = 0;
            int pageSize = 10;
            Pageable request = new PageRequest(pageNr, pageSize);
            Page<ActionItem> all = actionItemService.findByCategory(cat,request);
            for (ActionItem actionItem : all.getContent()) {
                this.mockMvc.perform(
                    get("/actionItem/detail/" + actionItem.getId())).andDo(print())
                    .andExpect(model().attributeExists("thisCategory"))
                    .andExpect(model().attributeExists("breadcrumb"))
                    .andExpect(model().attributeExists("actionItem"))
                    .andExpect(model().attribute("actionItem", notNullValue()))
                    .andExpect(model().attribute("actionItem", instanceOf(ActionItem.class)))
                    .andExpect(model().attribute("actionItem", hasProperty("id")))
                    .andExpect(model().attribute("actionItem", is(actionItemNotNullObject())));
            }
        }
    }


    @Test
    public void testEditDataFormCategory0() throws Exception {
        makeActiveUser(emails[0]);
        for(int i = 100; i<110; i++){
            ActionItem ai = new ActionItem();
            ai.setCategory(null);
            ai.setTitle("Title_"+i);
            ai.setText("Text_"+i);
            actionItemService.saveAndFlush(ai);
        }
        int pageNr = 0;
        int pageSize = 10;
        Pageable request = new PageRequest(pageNr, pageSize);
        Page<ActionItem> all = actionItemService.findByRootCategory(request);
        for (ActionItem actionItem : all.getContent()) {
            this.mockMvc.perform(
                    get("/actionItem/detail/" + actionItem.getId())).andDo(print())
                    .andExpect(model().attributeExists("thisCategory"))
                    .andExpect(model().attributeExists("breadcrumb"))
                    .andExpect(model().attributeExists("actionItem"))
                    .andExpect(model().attribute("actionItem", notNullValue()))
                    .andExpect(model().attribute("actionItem", instanceOf(ActionItem.class)))
                    .andExpect(model().attribute("actionItem", hasProperty("id")))
                    .andExpect(model().attribute("actionItem", is(actionItemNotNullObject())));
        }
        deleteAll();
    }

}