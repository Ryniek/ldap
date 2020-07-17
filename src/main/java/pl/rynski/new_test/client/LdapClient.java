package pl.rynski.new_test.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.*;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.rynski.new_test.model.User;

import javax.naming.Name;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

public class LdapClient {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ContextSource contextSource;
    @Autowired
    private LdapTemplate ldapTemplate;

    public DirContext authenticate(final String username, final String password) {
        DirContext ctx = null;
        ctx = contextSource.getContext("cn=" + username + ",ou=users," + "dc=example,dc=com", password);
        return ctx;
    }

    public List<String> search(final String username) {
        return ldapTemplate.search(
            "ou=users",
            "cn=" + username,
            (AttributesMapper<String>) attrs -> (String) attrs
                .get("cn")
                .get());
    }

    public List<User> findAll() {
        Name nameBase = LdapUtils.newLdapName("ou=users");
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        return ldapTemplate.findAll(nameBase, searchControls, User.class);
    }



    public void createUser(final String username, final String password) {
        Name dn = LdapNameBuilder
            .newInstance()
            .add("ou", "users")
            .add("cn", username)
            .build();
        DirContextAdapter context = new DirContextAdapter(dn);

        context.setAttributeValues("objectclass", new String[] { "top", "person", "organizationalPerson", "inetOrgPerson" });
        context.setAttributeValue("cn", username);
        context.setAttributeValue("sn", username);
        context.setAttributeValue("userPassword", passwordEncoder.encode(password));

        ldapTemplate.bind(context);
    }

    public void addRole(final String username, String role) {
        Name dn = LdapNameBuilder
            .newInstance()
            .add("ou", "groups")
            .add("cn", role)
            .build();
        DirContextOperations context = ldapTemplate.lookupContext(dn);
        context.addAttributeValue("uniqueMember", "cn=" + username + ",ou=users,dc=example,dc=com");
        ldapTemplate.modifyAttributes(context);
    }

    public void modify(final String username, final String password) {
        Name dn = LdapNameBuilder
            .newInstance()
            .add("ou", "users")
            .add("cn", username)
            .build();
        DirContextOperations context = ldapTemplate.lookupContext(dn);

        context.setAttributeValues("objectclass", new String[] { "top", "person", "organizationalPerson", "inetOrgPerson" });
        context.setAttributeValue("cn", username);
        context.setAttributeValue("sn", username);
        context.setAttributeValue("userPassword", password);

        ldapTemplate.modifyAttributes(context);
    }
}
