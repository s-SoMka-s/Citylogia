using Citylogia.Server.Core.Db.Implementations;
using Core.Api.Models;
using Core.Api.Models.Input;
using Core.Api.Models.Output;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using System.Linq;

namespace Core.Api
{
    [ApiController]
    [Route("/api/Users")]
    public class UserController : Controller
    {
        private readonly SqlContext context;

        public UserController(SqlContext context)
        {
            this.context = context;
        }

        [HttpPost("Auth/Email")]
        public bool Authorize([FromBody] AuthParameters parameters)
        {
            var repository = this.context.Users;
            var users = repository.ToList();

            var user = users.Find(u => u.Email == parameters.Email && u.Password == parameters.Password);
            if (user == default)
            {
                return false;
            }

            return true;
        }

        [HttpPost("Auth/Register")]
        public bool Register([FromBody] AuthParameters parameters)
        {
            var repository = this.context.Users;
            var users = repository.ToList();

            var user = users.Find(u => u.Email == parameters.Email);
            if (user != default)
            {
                return false;
            }

            var newUser = parameters.Build();
            repository.Add(newUser);
            this.context.SaveChanges();

            return true;
        }

        [HttpGet("")]
        public BaseCollectionResponse<UserSummary> GetUsers()
        {
            var users = this.context.Users.ToList();

            var summaries = new List<UserSummary>();

            foreach (var user in users)
            {
                var summary = new UserSummary(user);
                summaries.Add(summary);
            }

            var baseCollectionResponse = new BaseCollectionResponse<UserSummary>(summaries);

            return baseCollectionResponse;
        } 
    }
}
